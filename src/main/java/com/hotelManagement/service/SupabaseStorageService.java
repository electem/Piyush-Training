package com.hotelManagement.service;

import com.hotelManagement.repository.HotelImageRepository;
import com.hotelManagement.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupabaseStorageService {

    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.bucket}")
    private String bucket;

    @Value("${supabase.api.key}")
    private String serviceRoleKey;


    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadFile(MultipartFile file) throws IOException {

        // unique filename
        String fileName =UUID.randomUUID() + "_" + file.getOriginalFilename();

        // upload endpoint
        String uploadUrl = supabaseUrl +"/storage/v1/object/" + bucket +"/" +
                        fileName;

        // headers
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(serviceRoleKey);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // request body
        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

        // upload request
        ResponseEntity<String> response =
                restTemplate.exchange(
                        uploadUrl,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Image upload failed");
        }

        // public image url
        return supabaseUrl +
                "/storage/v1/object/public/" +
                bucket +
                "/" +
                fileName;
    }

    public void deleteFile(String fileUrl) {

        try {

            // extract file name from URL
            String fileName =
                    fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            String deleteUrl =
                    supabaseUrl +
                            "/storage/v1/object/" +
                            bucket +
                            "/" +
                            fileName;

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(serviceRoleKey);

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to delete file from storage"
            );
        }
    }
}