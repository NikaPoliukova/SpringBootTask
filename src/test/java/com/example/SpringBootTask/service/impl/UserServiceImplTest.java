package com.example.SpringBootTask.service.impl;


import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.Role;
import com.example.SpringBootTask.entity.User;
import com.example.SpringBootTask.repositiory.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void createUser_shouldReturnSavedUser() {
        UserDto dto = new UserDto("john", "pass", "john@example.com", Role.USER);
        User savedUser = User.builder()
                .username(dto.username())
                .password("encodedPass")
                .email(dto.email())
                .role(dto.role())
                .build();

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void getUserByUsername_existingUser_shouldReturnUser() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void getAllUser_shouldReturnList() {
        User user1 = new User();
        user1.setUsername("a");
        User user2 = new User();
        user2.setUsername("b");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUser();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_existingUser_shouldUpdateAndReturn() {
        UserDto dto = new UserDto("john", "newpass", "new@example.com", Role.USER);
        User existingUser = new User();
        existingUser.setUsername("john");
        existingUser.setPassword("oldPass");
        existingUser.setRole(Role.USER); // устанавливаем роль для существующего пользователя

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Optional<User> result = userService.updateUser(dto);

        assertTrue(result.isPresent());
        assertEquals("new@example.com", result.get().getEmail());
        assertEquals("newpass", result.get().getPassword());
        assertEquals(Role.USER, result.get().getRole()); // проверяем роль
        verify(userRepository).save(existingUser);
    }

    @Test
    void deleteUserByUsername_existingUser_shouldCallDelete() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        userService.deleteUserByUsername("john");

        verify(userRepository).deleteById(1L);
    }
}

