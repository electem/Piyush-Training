package com.jwt.authentication.controller;

import com.jwt.authentication.dto.UpdateUserRequest;
import com.jwt.authentication.entity.User;
import com.jwt.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> fetchById(@PathVariable Long id){
        return ResponseEntity.ok(userService.fetchById(id));
    }

}