package com.cyboul.demo.integration;

import com.cyboul.demo.config.JwtTestConfig;
import com.cyboul.demo.logic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JwtTestConfig.class })
@TestPropertySource(
        locations = { "classpath:application-test.properties" },
        properties = { "demo.jwt.expiration=1" } // override .properties
)
@ActiveProfiles("test")
public class JwtServiceExpireIntTest {

    @Autowired
    JwtService jwtService;

    @Autowired
    Clock clock;

    @Value("${demo.jwt.secret}")
    String secret;

    @Value("${demo.jwt.expiration}")
    long validityMillis;

    @Test
    void shouldNotValidateToken_whenTokenExpired() {

        String username = "Cyboul";
        UserDetails userDetails = User
                .withUsername(username)
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();

        Clock laterClock = Clock.offset(clock, Duration.ofMillis(10));
        JwtService validator = new JwtService(secret, validityMillis, laterClock);

        String token = jwtService.generateToken(username);
        boolean isValid = validator.validateToken(token, userDetails);

        assertFalse(isValid);
    }
}
