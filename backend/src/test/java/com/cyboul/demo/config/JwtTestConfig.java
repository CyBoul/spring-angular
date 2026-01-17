package com.cyboul.demo.config;

import com.cyboul.demo.logic.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Profile("test")
@TestConfiguration
public class JwtTestConfig {

    @Value("${demo.jwt.secret}") String secret;
    @Value("${demo.jwt.expiration}") long validityTimeMillis;

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    }

    @Bean
    public JwtService jwtService(Clock clock) {
        return new JwtService(secret, validityTimeMillis, clock);
    }

}
