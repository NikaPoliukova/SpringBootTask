package com.example.SpringBootTask.service;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User createUser(UserDto dto);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUser();

    Optional<User> updateUser(UserDto dto);

    void deleteUserByUsername(String username);
}