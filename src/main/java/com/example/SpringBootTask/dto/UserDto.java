package com.example.SpringBootTask.dto;

import com.example.SpringBootTask.entity.Role;
import lombok.Builder;

@Builder
public record UserDto(
        String username,
        String password,
        String email,
        Role role) {
}