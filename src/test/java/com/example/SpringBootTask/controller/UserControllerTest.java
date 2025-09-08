package com.example.SpringBootTask.controller;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.User;
import com.example.SpringBootTask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getUserByUsername_nonExistingUser_shouldReturn404() throws Exception {
        when(userService.getUserByUsername("john")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/user")
                        .param("username", "john"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("john");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("jane");
        user2.setEmail("jane@example.com");

        when(userService.getAllUser()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[1].username").value("jane"));
    }

    @Test
    void updateUser_existingUser_shouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("john");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpass");

        when(userService.updateUser(any(UserDto.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "john",
                                  "password": "newpass",
                                  "email": "new@example.com",
                                  "role": "USER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void updateUser_nonExistingUser_shouldReturn404() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "doesnotexist",
                                  "password": "pass",
                                  "email": "email@example.com",
                                  "role": "USER"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserByUsername_shouldReturn204() throws Exception {
        doNothing().when(userService).deleteUserByUsername("john");

        mockMvc.perform(delete("/api/v1/users/delete")
                        .param("username", "john"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserByUsername("john");
    }
}

