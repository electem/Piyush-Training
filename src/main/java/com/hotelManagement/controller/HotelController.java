package com.hotelManagement.controller;

import com.hotelManagement.dto.CreateHotelRequest;
import com.hotelManagement.dto.HotelRequest;
import com.hotelManagement.entity.Hotel;
import com.hotelManagement.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hotelManagement.dto.HotelResponse;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor

public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody CreateHotelRequest request) {
        System.out.println("API CALLED");
        Hotel hotel = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        List<HotelResponse> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long hotelId) {

        return hotelService.getHotelById(hotelId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long hotelId,
            @RequestBody HotelRequest request) {

        return hotelService.updateHotel(hotelId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @DeleteMapping("/{hotelId}")
//    public ResponseEntity<String> deleteHotel(@PathVariable Long hotelId) {
//
//        boolean deleted = hotelService.deleteHotel(hotelId);
//
//        if (!deleted) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok("Hotel deleted successfully");
//    }
}
