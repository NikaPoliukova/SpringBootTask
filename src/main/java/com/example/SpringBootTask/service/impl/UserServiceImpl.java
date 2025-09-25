package com.example.SpringBootTask.service.impl;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.Role;
import com.example.SpringBootTask.entity.User;
import com.example.SpringBootTask.repositiory.UserRepository;
import com.example.SpringBootTask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto dto) {
        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();
        User saved = userRepository.save(user);
        return toResponseDto(saved);
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toResponseDto);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public Optional<UserDto> updateUser(String username, UserDto dto) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (dto.email() != null) user.setEmail(dto.email());
                    if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));
                    User updated = userRepository.save(user);
                    return toResponseDto(updated);
                });
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> userRepository.deleteById(user.getUserId()));
    }

    private UserDto toResponseDto(User user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
