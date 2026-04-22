package com.example.CrudOperation.service;


import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ImageService {

    public String downloadAndSaveImage(String imageUrl) {

        try {
            String folderPath = "C:/Piyush/Upload/";

            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch image from URL");
            }

            String fileName = System.currentTimeMillis() + "_" +
                    imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            String filePath = folderPath + File.separator + fileName;

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(filePath)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Error downloading image", e);
        }
    }
}