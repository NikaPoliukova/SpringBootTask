package com.example.SpringBootTask.controller;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.User;
import com.example.SpringBootTask.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") @NotBlank @Size(min = 2, max = 60)
                                                  String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserDto userDto) {
        return userService.updateUser(userDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUserByUsername(@RequestParam("username")
                                                     @NotBlank @Size(min = 2, max = 60) String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}