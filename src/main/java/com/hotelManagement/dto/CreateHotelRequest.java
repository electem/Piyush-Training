package com.hotelManagement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHotelRequest(
        @NotBlank(message = "Hotel name is required")
        @Size(max = 150, message = "Hotel name must be at most 150 characters")
        String name,

        @NotBlank(message = "Location is required")
        @Size(max = 255, message = "Location must be at most 255 characters")
        String location,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotNull(message = "Rating is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
        @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
        Double rating
) {
}
