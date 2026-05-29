package com.jwt.authentication.service;

import com.jwt.authentication.dto.UpdateUserRequest;
import com.jwt.authentication.entity.User;
import com.jwt.authentication.exception.BadRequestException;
import com.jwt.authentication.exception.ResourceConflictException;
import com.jwt.authentication.exception.ResourceNotFoundException;
import com.jwt.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User fetchById(Long id){
            return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        if (request == null) {
            throw new BadRequestException("Update request cannot be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName().trim());
        }

        if (StringUtils.hasText(request.getEmail())) {
            String email = request.getEmail().trim();
            userRepository.findByEmail(email)
                    .filter(existingUser -> !existingUser.getId().equals(id))
                    .ifPresent(existingUser -> {
                        throw new ResourceConflictException("User already exists with email: " + email);
                    });
            user.setEmail(email);
        }

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }
}
