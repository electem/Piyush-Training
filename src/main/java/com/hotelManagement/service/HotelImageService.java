package com.hotelManagement.service;

import com.hotelManagement.dto.HotelImageResponse;
import com.hotelManagement.entity.Hotel;
import com.hotelManagement.entity.HotelImage;
import com.hotelManagement.repository.HotelImageRepository;
import com.hotelManagement.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelImageService {

    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;
    private final SupabaseStorageService supabaseStorageService;

    public String uploadHotelImage(Long hotelId, MultipartFile file) {

        try {

            // check hotel exists
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() ->
                            new RuntimeException("Hotel not found"));

            // upload image to supabase
            String imageUrl =supabaseStorageService.uploadFile(file);

            // save in DB
            HotelImage image = new HotelImage();

            image.setHotel(hotel);
            image.setImageName(file.getOriginalFilename());
            image.setImageUrl(imageUrl);

            hotelImageRepository.save(image);

            return imageUrl;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    public List<HotelImageResponse> getHotelImages(Long hotelId) {

        // check hotel exists
        if (!hotelRepository.existsById(hotelId)) {

            throw new RuntimeException("Hotel not found");
        }

        return hotelImageRepository.findByHotelId(hotelId)
                .stream()
                .map(image -> new HotelImageResponse(
                        image.getId(),
                        image.getImageUrl(),
                        image.getImageName()
                ))
                .collect(Collectors.toList());
    }

    public void deleteHotelImage(Long hotelId, Long imageId) {

        HotelImage image = hotelImageRepository
                .findByIdAndHotelId(imageId, hotelId)
                .orElseThrow(() ->
                        new RuntimeException("Image not found for this hotel"));

        // delete from storage (Supabase / S3)
        supabaseStorageService.deleteFile(image.getImageUrl());

        // delete from DB
        hotelImageRepository.delete(image);
    }

    public void deleteImages(Long hotelId, List<Long> imageIds) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        if (imageIds == null || imageIds.isEmpty()) {
            throw new RuntimeException("No image IDs provided");
        }

        List<HotelImage> imagesToDelete =
                hotelImageRepository.findByHotelIdAndIdIn(hotelId, imageIds);

        if (imagesToDelete.isEmpty()) {
            throw new RuntimeException("No matching images found for this hotel");
        }

        // 🔥 STEP ADDED: delete from storage
        for (HotelImage image : imagesToDelete) {
            supabaseStorageService.deleteFile(image.getImageUrl());
        }

        // DB delete
        hotelImageRepository.deleteAll(imagesToDelete);
    }

}