package integration;


import com.example.SpringBootTask.TestSpringBootTaskApplication;
import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.Role;
import com.example.SpringBootTask.entity.User;
import com.example.SpringBootTask.repositiory.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestSpringBootTaskApplication.class
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .username("testuser")
                .email("test@email.com")
                .password("encoded-pass")
                .role(Role.USER)
                .build());
    }

    @Test
    void getUserByUsername_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/user")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    void getUserByUsername_ShouldReturn404_WhenNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/user")
                        .param("username", "ghost"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() throws Exception {
        UserDto update = new UserDto("testuser", "newpass", "updated@email.com", Role.USER);

        mockMvc.perform(put("/api/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@email.com"));

        assertThat(userRepository.findByUsername("testuser"))
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo("updated@email.com");
    }

    @Test
    void deleteUser_ShouldRemoveUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/delete")
                        .param("username", "testuser"))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findByUsername("testuser")).isEmpty();
    }
}
