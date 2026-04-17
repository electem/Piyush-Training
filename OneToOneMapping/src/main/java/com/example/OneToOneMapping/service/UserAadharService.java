package com.example.OneToOneMapping.service;

import com.example.OneToOneMapping.entity.UserAadhar;
import com.example.OneToOneMapping.repository.UserAadharRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserAadharService {
    private UserAadharRepo userRepository;

    public UserAadhar saveUser(UserAadhar userAadhar) {
        return userRepository.save(userAadhar);
    }

    public List<UserAadhar> getAllUsers() {
        return userRepository.findAll();
    }

    public UserAadhar getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAadhar not found with id: " + id));
    }

    public UserAadhar updateUser(Long id, UserAadhar userAadhar) {
        UserAadhar existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAadhar not found with id: " + id));

        existingUser.setAddress(userAadhar.getAddress());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAadhar not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
