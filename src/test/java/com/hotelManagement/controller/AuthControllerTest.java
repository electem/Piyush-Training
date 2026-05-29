package com.hotelManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelManagement.dto.LoginRequest;
import com.hotelManagement.dto.LoginResponse;
import com.hotelManagement.dto.RegisterRequest;
import com.hotelManagement.exception.DuplicateEmailException;
import com.hotelManagement.exception.InvalidCredentialsException;
import com.hotelManagement.security.JwtAuthenticationFilter;
import com.hotelManagement.service.AuthService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.hotelManagement.exception.GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldReturnCreatedWhenRegistrationSucceeds() throws Exception {
        when(authService.registerUser(any())).thenReturn(new com.hotelManagement.dto.RegisterResponse(
                1L,
                "Piyush",
                "piyush@example.com",
                "USER",
                "jwt-token",
                Instant.parse("2026-05-23T12:00:00Z"),
                "Registration successful"
        ));

        RegisterRequest request = new RegisterRequest("Piyush", "piyush@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("piyush@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        RegisterRequest request = new RegisterRequest("", "invalid-email", "123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists())
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        when(authService.registerUser(any())).thenThrow(new DuplicateEmailException("Email already exists"));

        RegisterRequest request = new RegisterRequest("Piyush", "piyush@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void shouldReturnOkWhenLoginSucceeds() throws Exception {
        when(authService.loginUser(any())).thenReturn(new LoginResponse(
                1L,
                "Piyush",
                "piyush@example.com",
                "USER",
                "jwt-token",
                Instant.parse("2026-05-23T12:00:00Z"),
                "Login successful"
        ));

        LoginRequest request = new LoginRequest("piyush@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void shouldReturnBadRequestWhenLoginValidationFails() throws Exception {
        LoginRequest request = new LoginRequest("invalid-email", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        when(authService.loginUser(any())).thenThrow(new InvalidCredentialsException("Invalid email or password"));

        LoginRequest request = new LoginRequest("piyush@example.com", "wrong-password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}
