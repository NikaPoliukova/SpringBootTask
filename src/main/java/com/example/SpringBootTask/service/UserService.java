package com.example.SpringBootTask.service;

import com.example.SpringBootTask.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    UserDto createUser(UserDto dto);

    Optional<UserDto> getUserByUsername(String username);

    List<UserDto> getAllUsers();

    Optional<UserDto> updateUser(String username, UserDto dto);

    void deleteUserByUsername(String username);
}
