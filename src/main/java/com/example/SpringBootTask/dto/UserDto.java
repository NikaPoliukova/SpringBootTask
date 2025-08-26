package com.example.SpringBootTask.dto;

public record UserDto(
        String username,
        String password,
        String email) {
}