package com.hotelManagement.service;

import com.hotelManagement.dto.RoomImageResponse;
import com.hotelManagement.dto.RoomRequest;
import com.hotelManagement.dto.RoomResponse;
import com.hotelManagement.entity.Room;
import com.hotelManagement.repository.HotelRepository;
import com.hotelManagement.repository.RoomImageRepository;
import com.hotelManagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;

    public Optional<RoomResponse> addRoomToHotel(
            Long hotelId,
            RoomRequest request) {

        return hotelRepository.findById(hotelId)
                .map(hotel -> {

                    Room room = new Room();

                    room.setRoomNumber(request.getRoomNumber());
                    room.setRoomType(request.getRoomType());
                    room.setPrice(request.getPrice());
                    room.setAvailable(request.getAvailable());

                    room.setHotel(hotel);

                    Room savedRoom = roomRepository.save(room);

                    return new RoomResponse(
                            savedRoom.getId(),
                            savedRoom.getRoomNumber(),
                            savedRoom.getRoomType(),
                            savedRoom.getPrice(),
                            savedRoom.getAvailable()
                    );
                });
    }

    public Optional<List<RoomResponse>> getRoomsByHotelId(Long hotelId) {

        if (!hotelRepository.existsById(hotelId)) {
            return Optional.empty();
        }

        List<RoomResponse> rooms = roomRepository.findByHotelId(hotelId).stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getPrice(),
                        room.getAvailable()
                ))
                .collect(Collectors.toList());

        return Optional.of(rooms);
    }

    public Optional<RoomResponse> getRoomByHotelIdAndRoomId(Long hotelId, Long roomId) {

        return roomRepository.findById(roomId)
                .filter(room -> room.getHotel() != null
                        && room.getHotel().getId().equals(hotelId))
                .map(room -> {
                    List<RoomImageResponse> images = roomImageRepository.findByRoomId(roomId).stream()
                            .map(image -> new RoomImageResponse(
                                    image.getId(),
                                    image.getImageUrl(),
                                    image.getImageName()
                            ))
                            .collect(Collectors.toList());

                    return new RoomResponse(
                            room.getId(),
                            room.getRoomNumber(),
                            room.getRoomType(),
                            room.getPrice(),
                            room.getAvailable(),
                            images
                    );
                });
    }

    public Optional<RoomResponse> updateRoom(Long hotelId, Long roomId, RoomRequest request) {

        return roomRepository.findById(roomId)
                .filter(room -> room.getHotel() != null
                        && room.getHotel().getId().equals(hotelId))
                .map(room -> {

                    room.setRoomNumber(request.getRoomNumber());
                    room.setRoomType(request.getRoomType());
                    room.setPrice(request.getPrice());
                    room.setAvailable(request.getAvailable());

                    Room updated = roomRepository.save(room);

                    return new RoomResponse(
                            updated.getId(),
                            updated.getRoomNumber(),
                            updated.getRoomType(),
                            updated.getPrice(),
                            updated.getAvailable()
                    );
                });
    }

    public boolean deleteRoom(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            return false;
        }

        roomRepository.deleteById(roomId);

        return true;
    }
}
