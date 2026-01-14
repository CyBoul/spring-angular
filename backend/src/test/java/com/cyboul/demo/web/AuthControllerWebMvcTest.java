package com.cyboul.demo.web;

import com.cyboul.demo.config.MvcTestConfig;
import com.cyboul.demo.logic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * MVC Slice Test
 *
 * - Tests AuthController HTTP contract only
 * - Focus on request/response behavior and status codes
 * - Security simulated (no real authentication flow)
 * - Dependencies mocked:
 *   - AuthenticationManager,
 *   - UserDetailsService,
 *   - JwtService
 */

@WebMvcTest(AuthController.class)
@Import(MvcTestConfig.class)
public class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {

        UserDetails user = User.builder()
                .username("user@mail.com")
                .password("password")
                .authorities("USER")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user@mail.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userService.loadUserByUsername("user@mail.com")).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("TOKEN");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.com",
                          "password": "password"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, times(1)).loadUserByUsername(any());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void login_shouldReturnUnauthorized_whenWrongPassword() throws Exception {

        when(authenticationManager.authenticate(any()))
                .thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.com",
                          "password": "wrongPassword"
                        }
                        """))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldReturnUnauthorized_whenUserDoesNotExist() throws Exception {

        when(authenticationManager.authenticate(any()))
                .thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "unknow@mail.com",
                          "password": "password"
                        }
                        """))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }
}
