package com.cyboul.demo.logic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = { "demo.jwt.expiration=1" })
public class JwtServiceExpireIntTest {

    @Autowired
    JwtService jwtService;

    @Test
    void shouldNotValidateToken_whenTokenExpired() {

        String username = "Cyboul";
        UserDetails userDetails = User
                .withUsername(username)
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(username);
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }
}
