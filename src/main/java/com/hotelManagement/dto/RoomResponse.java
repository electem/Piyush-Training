package com.hotelManagement.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor

public class RoomResponse {
    private Long id;
    private Long roomNumber;
    private String roomType;
    private Double price;
    private Boolean available;
    private List<RoomImageResponse> images;

    public RoomResponse(Long id, Long roomNumber, String roomType, Double price, Boolean available) {
        this(id, roomNumber, roomType, price, available, List.of());
    }

    public RoomResponse(Long id, Long roomNumber, String roomType, Double price, Boolean available, List<RoomImageResponse> images) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.available = available;
        this.images = images;
    }
}

