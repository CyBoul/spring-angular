package com.cyboul.demo.web;

import com.cyboul.demo.config.TestSecurityConfig;
import com.cyboul.demo.logic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Slice MVC / Partial Integration (without DB, Repo, JPA)
 */

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {

        String email = "user@mail.com";

        when(jwtService.generateToken(email)).thenReturn("TOKEN");

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
    }

    @Test
    void login_shouldReturnBadRequest_whenPasswordIsInvalid() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.com",
                          "password": "wrongPassword"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturnBadRequest_whenUserDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "unknow@mail.com",
                          "password": "password"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }
}
