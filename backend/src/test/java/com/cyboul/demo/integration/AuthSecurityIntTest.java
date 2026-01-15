package com.cyboul.demo.integration;

import com.cyboul.demo.config.JwtTestConfig;
import com.cyboul.demo.config.SecurityTestConfig;
import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.logic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * Security-focused Integration Test
 *
 * - Purpose: validate HTTP security behavior (status codes, role access, JWT auth)
 * - Context: near-real Spring Boot context with MockMvc
 * - Scope: no service/business logic verification
 * - Repo/DB interactions are mocked as needed for controller flow
 */

@SpringBootTest
@AutoConfigureMockMvc
@Import({JwtTestConfig.class, SecurityTestConfig.class})
@ActiveProfiles("test")
public class AuthSecurityIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @MockitoBean
    PetRepository petRepository;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {

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
    void getPets_shouldReturn200_whenJwtIsValid() throws Exception {

        when(petRepository.findAll()).thenReturn(new ArrayList<>());

        performGetPetsWithToken(jwtService.generateToken("user@mail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void getPets_shouldReturn401_whenJwtIsNotValid() throws Exception {

        performGetPetsWithToken("jwt.invalid.token")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPets_shouldReturn401_whenNoTokenProvided() throws Exception {

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deletePetById_shouldReturn204_withAdminToken() throws Exception {

        when(petRepository.existsById(1L)).thenReturn(true);
        doNothing().when(petRepository).deleteById(1L);

        performDeletePetWithToken(jwtService.generateToken("admin@mail.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePetById_shouldReturn403_withUserToken() throws Exception {

        performDeletePetWithToken(jwtService.generateToken("user@mail.com"))
                .andExpect(status().isForbidden());
    }

    private ResultActions performAuthenticatedRequest(
            MockHttpServletRequestBuilder builder, String token ) throws Exception {

        return mockMvc.perform(builder
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
    }

    private ResultActions performDeletePetWithToken(String token) throws Exception {
        return performAuthenticatedRequest(delete("/api/pets/1"), token);
    }

    private ResultActions performGetPetsWithToken(String token) throws Exception {
        return performAuthenticatedRequest(get("/api/pets"), token);
    }

}
