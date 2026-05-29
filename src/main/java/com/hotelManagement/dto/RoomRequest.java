package com.hotelManagement.dto;

import lombok.Data;

@Data
public class RoomRequest {

        private Long roomNumber;

        private String roomType;

        private Double price;

        private Boolean available;

}
