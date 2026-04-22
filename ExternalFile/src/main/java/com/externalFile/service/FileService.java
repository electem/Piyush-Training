package com.externalFile.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private final String uploadDir = "C:/Piyush/Upload/";

    public String saveFile(MultipartFile file) throws IOException {

        // create folder if not exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // file path
        String filePath = uploadDir + file.getOriginalFilename();

        // save file
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;

    }

//    public String downloadAndSaveFile(String fileUrl) {
//        try {
//            // Call external API
//            byte[] fileData = restTemplate.getForObject(fileUrl, byte[].class);
//
//            // Folder path (create if not exists)
//            String folderPath = "C:/uploads/";
//            File folder = new File(folderPath);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            // File name
//            String fileName = "downloaded_file_" + System.currentTimeMillis() + ".jpg";
//
//            // Save file
//            File file = new File(folderPath + fileName);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(fileData);
//            fos.close();
//
//            return "File saved successfully: " + file.getAbsolutePath();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving file", e);
//        }
//    }
}

