package com.example.SpringBootTask.controller;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class AuthController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        boolean exists = userService.getUserByUsername(userDto.username()).isPresent();
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username '" + userDto.username() + "' already exists");
        }
        var createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User '" + createdUser.username() + "' successfully registered");
    }
}