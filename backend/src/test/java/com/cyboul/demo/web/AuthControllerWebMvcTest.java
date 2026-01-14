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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
                .username("user@email.com")
                .password("password")
                .authorities("USER")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.generateToken(user.getUsername())).thenReturn("TOKEN");

        performLogin(user.getUsername(), user.getPassword())
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

        performLogin("user@mail.com","wrongPassword")
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_shouldReturnUnauthorized_whenUserDoesNotExist() throws Exception {

        when(authenticationManager.authenticate(any()))
                .thenThrow(UsernameNotFoundException.class);

        performLogin("unknown@mail.com","password")
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    private ResultActions performLogin(String email, String password) throws Exception {

        return mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                {
                  "email": "%s",
                  "password": "%s"
                }
                """, email, password)));
    }

}
