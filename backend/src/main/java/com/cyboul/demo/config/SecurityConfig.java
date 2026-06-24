package com.cyboul.demo.config;

import com.cyboul.demo.logic.service.JwtService;
import com.cyboul.demo.web.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] DEFAULT_NAV = { "/", "/api/auth/login", "/api/auth/logout" };
    private static final String[] ANGU_ASSETS = { "/index.html", "/*.css", "/*.js" };
    private static final String[] SPA_ROUTES  = { "/login", "/pets", "/admin/**" };
    private static final String[] API_DOCS    = { "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**" };

    @Value("${app.cors.allowed-origins:}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter,
                                                   Environment env) throws Exception {

        boolean swaggerPublic = Arrays.asList(env.getActiveProfiles()).contains("dev");

        http.authorizeHttpRequests(requests -> {
                    requests.requestMatchers(DEFAULT_NAV).permitAll()
                            .requestMatchers(ANGU_ASSETS).permitAll()
                            .requestMatchers(SPA_ROUTES).permitAll();

                    if (swaggerPublic) {
                        requests.requestMatchers(API_DOCS).permitAll();
                    }

                    requests.anyRequest().authenticated();
                })

                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .headers(headers -> headers
                     .contentSecurityPolicy(csp -> csp
                          .policyDirectives("default-src 'self'; "
                               + "script-src 'self'; "
                               + "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; "
                               + "font-src 'self' https://fonts.gstatic.com; "
                               + "img-src 'self' data:; "
                               + "connect-src 'self'")))

                .sessionManagement(session -> session
                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex
                     .authenticationEntryPoint(
                          (request, response, authException) ->
                          response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                     ))

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter(UserDetailsService userService, JwtService jwtService) {
        return new JwtFilter(userService, jwtService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        if (!allowedOrigins.isBlank()) {
            config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        }
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
