package com.hotelManagement.controller;

import com.hotelManagement.service.HotelImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hotel-images")
@RequiredArgsConstructor
public class HotelImageController {

    private final HotelImageService hotelImageService;

    @PostMapping(value = "/{hotelId}/images",
            consumes = "multipart/form-data")
    public ResponseEntity<?> uploadHotelImage(
            @PathVariable Long hotelId,
            @RequestParam("file") MultipartFile file) {

        try {
            String imageUrl = hotelImageService.uploadHotelImage(hotelId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        @GetMapping("/hotels/{hotelId}/images")
        public ResponseEntity<?> getHotelImages(@PathVariable Long hotelId) {
            try {
                return ResponseEntity.ok(hotelImageService.getHotelImages(hotelId));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    @DeleteMapping("/{hotelId}/images/{imageId}")
    public ResponseEntity<?> deleteHotelImage(
            @PathVariable Long hotelId,
            @PathVariable Long imageId) {

        try {
            hotelImageService.deleteHotelImage(hotelId, imageId);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{hotelId}/images/delete-bulk")
    public ResponseEntity<?> deleteMultipleImages(
            @PathVariable Long hotelId,
            @RequestBody List<Long> imageIds) {
        hotelImageService.deleteImages(hotelId, imageIds);
        return ResponseEntity.ok("Deleted successfully");
    }
}