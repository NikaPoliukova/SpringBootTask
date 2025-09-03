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

    public User createUser(UserDto dto) {
        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> updateUser(UserDto dto) {
        if (dto.username() == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(dto.username())
                .map(user -> {
                    if (dto.email() != null)
                        user.setEmail(dto.email());
                    if (dto.password() != null) {
                        user.setPassword(dto.password());
                    }
                    return userRepository.save(user);
                });
    }


    public void deleteUserByUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    userRepository.deleteById(user.getUserId());
                });
    }
}
