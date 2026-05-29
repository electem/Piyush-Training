package com.hotelManagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelManagement.dto.*;
import com.hotelManagement.entity.Hotel;
import com.hotelManagement.entity.HotelImage;
import com.hotelManagement.entity.Room;
import com.hotelManagement.repository.HotelImageRepository;
import com.hotelManagement.repository.HotelRepository;
import com.hotelManagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelImageRepository imageRepo;
    private final RoomRepository roomRepo;
    private final ObjectMapper objectMapper;

    @Transactional
    public Hotel createHotel(CreateHotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.name().trim());
        hotel.setLocation(request.location().trim());
        hotel.setDescription(request.description().trim());
        hotel.setRating(request.rating());

        return hotelRepository.save(hotel);
    }

    public List<HotelResponse> getAllHotels() {

        try {

            List<HotelDetailsProjection> hotels =
                    hotelRepository.getAllHotelsData();

            return hotels.stream().map(h -> {

                List<RoomResponse> rooms =
                        null;
                try {
                    rooms = objectMapper.readValue(
                            h.getRooms(),
                            new TypeReference<List<RoomResponse>>() {}
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                List<HotelImageResponse> images =
                        null;
                try {
                    images = objectMapper.readValue(
                            h.getImages(),
                            new TypeReference<List<HotelImageResponse>>() {}
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                return new HotelResponse(
                        h.getId(),
                        h.getName(),
                        h.getLocation(),
                        h.getDescription(),
                        h.getRating(),
                        h.getCreatedAt(),
                        images,
                        rooms.size(),
                        rooms
                );

            }).toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional(readOnly = true)
    public Optional<HotelResponse> getHotelById(Long hotelId) {

        try {

            HotelDetailsProjection h =
                    hotelRepository.getHotelDetails(hotelId);

            if (h == null) {
                return Optional.empty();
            }

            List<RoomResponse> rooms =
                    objectMapper.readValue(
                            h.getRooms(),
                            new TypeReference<List<RoomResponse>>() {}
                    );

            List<HotelImageResponse> images =
                    objectMapper.readValue(
                            h.getImages(),
                            new TypeReference<List<HotelImageResponse>>() {}
                    );

            HotelResponse response = new HotelResponse(
                    h.getId(),
                    h.getName(),
                    h.getLocation(),
                    h.getDescription(),
                    h.getRating(),
                    h.getCreatedAt(),
                    images,
                    rooms.size(),
                    rooms
            );

            return Optional.of(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<HotelResponse> updateHotel(Long hotelId, HotelRequest request) {

        return hotelRepository.findById(hotelId)
                .map(hotel -> {

                    hotel.setName(request.getName());
                    hotel.setLocation(request.getLocation());
                    hotel.setDescription(request.getDescription());
                    hotel.setRating(request.getRating());

                    Hotel updatedHotel = hotelRepository.save(hotel);

                    List<HotelImageResponse> images =
                            updatedHotel.getImages() == null
                                    ? List.of()
                                    : updatedHotel.getImages().stream()
                                      .map(img -> new HotelImageResponse(
                                              img.getId(),
                                              img.getImageUrl(),
                                              img.getImageName()))
                                      .collect(Collectors.toList());

                    List<RoomResponse> rooms =
                            updatedHotel.getRooms() == null
                                    ? List.of()
                                    : updatedHotel.getRooms().stream()
                                      .map(room -> new RoomResponse(
                                              room.getId(),
                                              room.getRoomNumber(),
                                              room.getRoomType(),
                                              room.getPrice(),
                                              room.getAvailable()))
                                      .collect(Collectors.toList());

                    return new HotelResponse(
                            updatedHotel.getId(),
                            updatedHotel.getName(),
                            updatedHotel.getLocation(),
                            updatedHotel.getDescription(),
                            updatedHotel.getRating(),
                            updatedHotel.getCreatedAt(),
                            images,
                            rooms.size(),
                            rooms
                    );
                });
    }

//    @Transactional
//    public boolean deleteHotel(Long hotelId) {
//
//        Optional<Hotel> optionalHotel = hotelRepository.findHotelWithImagesAndRooms(hotelId);
//
//        if (optionalHotel.isEmpty()) {
//            return false;
//        }
//
//        Hotel hotel = optionalHotel.get();
//
//        // Delete images from storage
//        if (hotel.getImages() != null) {
//
//            for (HotelImage image : hotel.getImages()) {
//
//                // Example:
//                // fileStorageService.deleteFile(image.getImageUrl());
//
//                deleteImageFromStorage(image.getImageUrl());
//            }
//        }
//
//        // Delete hotel
//        // Rooms and images will be deleted automatically using cascade
//        hotelRepository.delete(hotel);
//
//        return true;
//    }
//    private void deleteImageFromStorage(String imageUrl) {
//
//        try {
//
//            Path filePath = Paths.get("uploads")
//                    .resolve(Paths.get(imageUrl).getFileName());
//
//            Files.deleteIfExists(filePath);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to delete image file", e);
//        }
//    }
}
