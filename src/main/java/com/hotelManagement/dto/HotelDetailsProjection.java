package com.hotelManagement.dto;

import java.time.LocalDateTime;

public interface HotelDetailsProjection {

    Long getId();
    String getName();
    String getLocation();
    String getDescription();
    Double getRating();

    LocalDateTime getCreatedAt();

    String getRooms();

    String getImages();
}