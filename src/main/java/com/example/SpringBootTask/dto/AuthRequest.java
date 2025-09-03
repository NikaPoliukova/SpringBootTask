package com.example.SpringBootTask.dto;

public record AuthRequest(
        String userName,
        String password) {
}