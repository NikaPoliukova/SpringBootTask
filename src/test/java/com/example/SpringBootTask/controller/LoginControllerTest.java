//package com.example.SpringBootTask.controller;
//
//
//import com.example.SpringBootTask.dto.AuthRequest;
//import com.example.SpringBootTask.entity.User;
//import com.example.SpringBootTask.security.JwtUtils;
//import com.example.SpringBootTask.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class LoginControllerTest {
//
//    @Mock
//    private JwtUtils jwtUtils;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private LoginController loginController;
//
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private AutoCloseable autoCloseable;
//
//    @BeforeEach
//    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//    @Test
//    void login_validCredentials_shouldReturnToken() throws Exception {
//        AuthRequest request = new AuthRequest("john", "pass");
//        User user = new User();
//        user.setUsername("john");
//
//        when(userService.getUserByUsername("john")).thenReturn(Optional.of(user));
//        when(jwtUtils.generateToken(user)).thenReturn("mockToken");
//
//        mockMvc.perform(post("/api/v1/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mockToken"));
//
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//    }
//
//    @Test
//    void login_invalidCredentials_shouldReturnUnauthorized() throws Exception {
//        AuthRequest request = new AuthRequest("john", "wrongpass");
//
//        doThrow(BadCredentialsException.class)
//                .when(authenticationManager)
//                .authenticate(any(UsernamePasswordAuthenticationToken.class));
//
//        mockMvc.perform(post("/api/v1/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized());
//    }
//}
