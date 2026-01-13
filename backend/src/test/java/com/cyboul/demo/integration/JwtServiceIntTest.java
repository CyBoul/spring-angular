package com.cyboul.demo.integration;

import com.cyboul.demo.config.JwtTestConfig;
import com.cyboul.demo.logic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest // The entire Spring context (not wanted)
//@Import(.class) // explicit additions to Spring context (not wanted)
@ExtendWith(SpringExtension.class) // minimal context for Tests (exclude DB config..)
@ContextConfiguration(classes = { JwtTestConfig.class })
@TestPropertySource(locations = { "classpath:application-test.properties" })
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


