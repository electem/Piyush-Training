package com.hotelManagement.service;

import com.hotelManagement.dto.LoginRequest;
import com.hotelManagement.dto.LoginResponse;
import com.hotelManagement.dto.RegisterRequest;
import com.hotelManagement.dto.RegisterResponse;
import com.hotelManagement.entity.User;
import com.hotelManagement.exception.DuplicateEmailException;
import com.hotelManagement.exception.InvalidCredentialsException;
import com.hotelManagement.repository.UserRepository;
import com.hotelManagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return DEFAULT_ROLE;
        }
        return role.trim().toUpperCase(Locale.ROOT);
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateEmailException("Email already exists");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(DEFAULT_ROLE);

        User savedUser = userRepository.save(user);
        String normalizedRole = normalizeRole(savedUser.getRole());
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail(), normalizedRole);
        Instant expiresAt = jwtService.getExpiryInstant();

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                normalizedRole,
                token,
                expiresAt,
                "Registration successful"
        );
    }


    public LoginResponse loginUser(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String normalizedRole = normalizeRole(user.getRole());
        String token = jwtService.generateToken(user.getId(), user.getEmail(), normalizedRole);
        Instant expiresAt = jwtService.getExpiryInstant();

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                normalizedRole,
                token,
                expiresAt,
                "Login successful"
        );
    }
}
