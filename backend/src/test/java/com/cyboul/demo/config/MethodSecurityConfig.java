package com.cyboul.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@TestConfiguration
@EnableMethodSecurity
public class MethodSecurityConfig {
    // just for @EnableMethodSecurity in webmvc tests
}
