package com.jwt.authentication.service;

import com.jwt.authentication.dto.AuthRequest;
import com.jwt.authentication.dto.AuthResponse;
import com.jwt.authentication.entity.User;
import com.jwt.authentication.exception.BadRequestException;
import com.jwt.authentication.exception.ResourceConflictException;
import com.jwt.authentication.repository.UserRepository;
import com.jwt.authentication.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        validateCredentials(request);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceConflictException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setName(StringUtils.hasText(request.getName()) ? request.getName().trim() : request.getEmail());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return buildAuthResponse(user.getEmail());
    }

    public AuthResponse login(AuthRequest request) {
        validateCredentials(request);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail().trim(), request.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw new BadRequestException("Invalid email or password");
        }

        return buildAuthResponse(request.getEmail().trim());
    }

    private void validateCredentials(AuthRequest request) {
        if (request == null || !StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            throw new BadRequestException("Email and password are required");
        }
    }

    private AuthResponse buildAuthResponse(String email) {
        String token = jwtUtil.generateToken(email);

        return new AuthResponse(
                token,
                "Bearer",
                jwtUtil.getExpiration(),
                jwtUtil.extractExpiration(token)
        );
    }
}