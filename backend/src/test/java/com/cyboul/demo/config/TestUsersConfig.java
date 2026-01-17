package com.cyboul.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestUsersConfig {

    @Bean
    @Primary // Prior to UserService for tests purpose
    public UserDetailsService userService(PasswordEncoder encoder) {
        UserDetails user = createTestUser("user@mail.com", "password", encoder);
        UserDetails admin = User.builder()
                .username("admin@mail.com")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    public static UserDetails createTestUser(String email, String password, PasswordEncoder encoder) {
        return User.builder()
                .username(email)
                .password(encoder.encode(password))
                .roles("USER")
                .build();
    }

}