package com.hotelManagement.controller;

import com.hotelManagement.dto.RoomRequest;
import com.hotelManagement.dto.RoomImageResponse;
import com.hotelManagement.dto.RoomResponse;
import com.hotelManagement.service.RoomImageService;
import com.hotelManagement.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
public class RoomController {


    private final RoomService roomService;
    private final RoomImageService roomImageService;


    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<RoomResponse> addRoomToHotel(
            @PathVariable Long hotelId,
            @RequestBody RoomRequest request) {

        return roomService.addRoomToHotel(hotelId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<RoomResponse>> getRoomsByHotelId(@PathVariable Long hotelId) {

        return roomService.getRoomsByHotelId(hotelId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponse> getRoomByHotelIdAndRoomId(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {

        return roomService.getRoomByHotelIdAndRoomId(hotelId, roomId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @RequestBody RoomRequest request) {

        return roomService.updateRoom(hotelId, roomId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{hotelId}/rooms/{roomId}/images", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadRoomImage(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @RequestParam("file") MultipartFile file) {

        try {
            return ResponseEntity.ok(
                    roomImageService.uploadRoomImage(hotelId, roomId, file)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {

        boolean deleted = roomService.deleteRoom(roomId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
