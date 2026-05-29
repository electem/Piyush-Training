package com.hotelManagement.service;

import com.hotelManagement.dto.RoomImageResponse;
import com.hotelManagement.entity.Room;
import com.hotelManagement.entity.RoomImage;
import com.hotelManagement.repository.RoomImageRepository;
import com.hotelManagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RoomImageService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final SupabaseStorageService supabaseStorageService;

    public RoomImageResponse uploadRoomImage(Long hotelId, Long roomId, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is required");
            }

            Room room = roomRepository.findById(roomId)
                    .filter(existingRoom ->
                            existingRoom.getHotel() != null
                                    && existingRoom.getHotel().getId().equals(hotelId))
                    .orElseThrow(() -> new RuntimeException("Room not found for this hotel"));

            String imageUrl = supabaseStorageService.uploadFile(file);

            RoomImage image = new RoomImage();
            image.setRoom(room);
            image.setImageName(file.getOriginalFilename());
            image.setImageUrl(imageUrl);

            RoomImage savedImage = roomImageRepository.save(image);

            return new RoomImageResponse(
                    savedImage.getId(),
                    savedImage.getImageUrl(),
                    savedImage.getImageName()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
