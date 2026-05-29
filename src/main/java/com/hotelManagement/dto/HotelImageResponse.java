package com.hotelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelImageResponse {
    private Long id;
    private String imageUrl;
    private String imageName;
}

