package com.example.SpringBootTask.controller;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public UserDto getUserByUsername(
            @PathVariable @NotBlank @Size(min = 2, max = 60) String username) {
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{username}")
    public UserDto updateUser(
            @PathVariable @NotBlank @Size(min = 2, max = 60) String username,
            @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(username, userDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable @NotBlank @Size(min = 2, max = 60) String username) {
        userService.deleteUserByUsername(username);
    }
}
