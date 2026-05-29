package com.hotelManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelPdfDto {

    private String name;
    private String location;
    private String description;
    private Double rating;

    private List<ImageDTO> images;
    private List<RoomDTO> rooms;
}
