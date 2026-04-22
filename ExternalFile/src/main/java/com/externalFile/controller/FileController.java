package com.externalFile.controller;

import com.externalFile.service.FileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

//    @PostMapping("/download")
//    public String downloadFile(@RequestParam String url) {
//        return fileService.downloadAndSaveFile(url);
//    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String path = fileService.saveFile(file);
            return "File saved at: " + path;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}