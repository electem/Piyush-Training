package com.example.OneToOneMapping.controller;

import com.example.OneToOneMapping.entity.UserAadhar;
import com.example.OneToOneMapping.service.UserAadharService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userAadhar")
public class UserAadharController {
    
    private UserAadharService userService;

    @PostMapping
    public UserAadhar createUser(@RequestBody UserAadhar userAadhar) {
        return userService.saveUser(userAadhar);
    }

    @GetMapping
    public List<UserAadhar> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserAadhar getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserAadhar updateUser(@PathVariable Long id, @RequestBody UserAadhar userAadhar) {
        return userService.updateUser(id, userAadhar);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "UserAadhar deleted";
    }
}
