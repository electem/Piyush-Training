package com.hotelManagement.controller;

import com.hotelManagement.dto.LoginRequest;
import com.hotelManagement.dto.LoginResponse;
import com.hotelManagement.dto.RegisterRequest;
import com.hotelManagement.dto.RegisterResponse;
import com.hotelManagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
