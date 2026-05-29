package com.hotelManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelPdfResponse {

    private Long id;
    private String name;
    private String location;
    private String description;
    private Double rating;

    private List<HotelImageResponse> images;
    private List<RoomResponse> rooms;
}
