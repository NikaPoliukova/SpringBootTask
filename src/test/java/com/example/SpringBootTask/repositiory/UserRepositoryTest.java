package com.example.SpringBootTask.repositiory;

import com.example.SpringBootTask.entity.Role;
import com.example.SpringBootTask.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_existingUser_shouldReturnUser() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setRole(Role.USER);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("john");
        assertTrue(found.isPresent());
        assertEquals("john@example.com", found.get().getEmail());
    }

    @Test
    void findByUsername_nonExistingUser_shouldReturnEmpty() {
        Optional<User> found = userRepository.findByUsername("doesnotexist");
        assertTrue(found.isEmpty());
    }
}