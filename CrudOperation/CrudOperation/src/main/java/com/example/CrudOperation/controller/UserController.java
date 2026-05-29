package com.example.CrudOperation.controller;

import com.example.CrudOperation.entity.User;
import com.example.CrudOperation.service.ImageService;
import com.example.CrudOperation.service.PdfService;
import com.example.CrudOperation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {

        User user=  userService.getUserById(id);

       return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted";
    }

    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadUsersPdf() {

        // Fetch all users
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Generate PDF
        byte[] pdf = pdfService.generateUserPdf(users);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/save/pdf")
    public ResponseEntity<String> saveUsersPdf() {

        // Fetch users
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Provide folder path (you can change this)
        String folderPath = "C:/Piyush/Upload/";


        // Generate & save PDF
        String filePath = pdfService.generateAndSavePdf(users, folderPath);

        return ResponseEntity.ok("PDF saved at: " + filePath);
    }

    @PostMapping("/download-image")
    public ResponseEntity<String> downloadImage(@RequestParam String imageUrl) {

        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Image URL is required");
        }

        try {
            String savedPath = imageService.downloadAndSaveImage(imageUrl);
            return ResponseEntity.ok("Image saved at: " + savedPath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to download image: " + e.getMessage());
        }
    }
}
