package com.hotelManagement.dto;

import lombok.Data;

@Data
public class HotelRequest {

    private String name;
    private String location;
    private String description;
    private Double rating;
}
