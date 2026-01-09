package com.cyboul.demo.logic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class JwtServiceIntTest {

    @Autowired
    JwtService jwtService;

    @Test
    void shouldGenerateAndValidateToken_inSpringContext() {

        String username = "Cyboul";
        UserDetails userDetails = User
                .withUsername(username)
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(username);
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void shouldGenerateButNotValidateToken_whenWrongUsername() {

        String username = "Cyboul";
        String wrongUsername = "Luobyc";
        UserDetails userDetails = User
                .withUsername(username)
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();


        String token = jwtService.generateToken(wrongUsername);
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }
}


