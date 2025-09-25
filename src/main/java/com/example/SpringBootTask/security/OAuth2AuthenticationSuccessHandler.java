package com.example.SpringBootTask.security;

import com.example.SpringBootTask.dto.UserDto;
import com.example.SpringBootTask.entity.Role;
import com.example.SpringBootTask.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String username = Optional.ofNullable((String) oAuth2User.getAttributes().get("login"))
                .orElse((String) oAuth2User.getAttributes().get("preferred_username"));

        String email = (String) oAuth2User.getAttributes().get("email");

        String tempPassword = UUID.randomUUID().toString();

        UserDto dto = UserDto.builder()
                .username(username)
                .email(email)
                .password(tempPassword)
                .role(Role.USER)
                .build();

        UserDto userResponse = userService.getUserByUsername(username)
                .orElseGet(() -> userService.createUser(dto));


        String token = jwtUtils.generateToken(userResponse.username(), userResponse.role());

        Cookie cookie = new Cookie("ACCESS_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}