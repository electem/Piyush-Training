package com.hotelManagement.dto;

import java.time.Instant;

public record LoginResponse(
        Long userId,
        String name,
        String email,
        String role,
        String token,
        Instant expiresAt,
        String message
) {
}
