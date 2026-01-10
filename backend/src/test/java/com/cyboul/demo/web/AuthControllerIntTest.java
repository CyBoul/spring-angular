package com.cyboul.demo.web;

import com.cyboul.demo.config.TestSecurityConfig;
import com.cyboul.demo.logic.service.JwtService;
import com.cyboul.demo.logic.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest()
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { AuthController.class, TestSecurityConfig.class })
@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
//@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    JwtService jwtService;

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
    void login_shouldReturnBadRequest_whenCredentialsAreInvalid() throws Exception {

//        String email = "user@mail.com";
//        String password = "password";
//
//        when(userService.loadUserByEmail(email))
//                .thenReturn(User.builder()
//                        .username(email)
//                        .password(password)
//                        .authorities("USER")
//                        .build());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.com",
                          "password": "wrongPassword"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
