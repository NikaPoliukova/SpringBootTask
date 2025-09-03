package com.example.SpringBootTask.controller;

import com.example.SpringBootTask.config.CustomMetrics;
import com.example.SpringBootTask.dto.AuthRequest;
import com.example.SpringBootTask.dto.AuthResponse;
import com.example.SpringBootTask.security.JwtUtils;
import com.example.SpringBootTask.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {

    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomMetrics metrics;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.userName(), request.password())
            );
            var user = userService.getUserByUsername(request.userName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var token = jwtUtil.generateToken(user);

            metrics.incrementSuccessfulLogins();

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            metrics.incrementFailedLogins();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid username or password"));

        } catch (UsernameNotFoundException e) {
            metrics.incrementFailedLogins();

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(e.getMessage()));
        }
    }
}
