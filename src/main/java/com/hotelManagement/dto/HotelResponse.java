package com.hotelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String location;
    private String description;
    private Double rating;
    private LocalDateTime createdAt;
    private List<HotelImageResponse> images;
    private Integer totalRoomsCount;
    private List<com.hotelManagement.dto.RoomResponse> rooms;
}

