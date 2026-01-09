package com.cyboul.demo.logic.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTest {

    @Test
    public void generateToken_shouldContainUsernameAndExpiration(){

        long ttl = 60_000L;
        Instant fixedInstant = Instant.parse("2025-01-01T10:00:00Z");
        Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        String secret = "oJrytFKJdT4p/ZDvQL6i1UVQFOHcPvihxauTaSTRXyM=";
        String username = "Cyboul";

        JwtService jwtService = new JwtService(secret, ttl, clock);

        String token = jwtService.generateToken(username);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        Date expire = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        assertNotNull(token);
        assertNotNull(expire);
        assertEquals(username, jwtService.extractUsername(token));
        assertEquals(expire.toInstant(), clock.instant().plusMillis(ttl));
    }

    @Test
    public void validateToken_shouldReturnTrue_whenUsernameMatchesAndTokenNotExpired(){


        long ttl = 60_000L;
        Instant fixedInstant = Instant.parse("2025-01-01T10:00:00Z");
        Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        String secret = "oJrytFKJdT4p/ZDvQL6i1UVQFOHcPvihxauTaSTRXyM=";
        String username = "Cyboul";

        JwtService jwtService = new JwtService(secret, ttl, clock);
        String token = jwtService.generateToken(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void validateToken_shouldReturnFalse_whenTokenExpired(){

        long ttl = 60_000L;
        Instant t0 = Instant.parse("2025-01-01T10:00:00Z");
        Clock clock = Clock.fixed(t0, ZoneOffset.UTC);
        Clock validClock = Clock.offset(clock, Duration.ofMinutes(2));

        String secret = "oJrytFKJdT4p/ZDvQL6i1UVQFOHcPvihxauTaSTRXyM=";
        String username = "Cyboul";

        JwtService jwtServiceGen = new JwtService(secret, ttl, clock);
        String expiredToken = jwtServiceGen.generateToken(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = new JwtService(secret, ttl, validClock)
                .validateToken(expiredToken, userDetails);

        assertFalse(isValid);
    }

    @Test
    public void validateToken_shouldReturnFalse_whenUsernameNotMatching(){

        long ttl = 60_000L;
        Instant t0 = Instant.parse("2025-01-01T10:00:00Z");
        Clock clock = Clock.fixed(t0, ZoneOffset.UTC);

        String secret = "oJrytFKJdT4p/ZDvQL6i1UVQFOHcPvihxauTaSTRXyM=";
        String username = "Cyboul";
        String wrongUsername = "Luobyc";

        JwtService jwtService = new JwtService(secret, ttl, clock);
        String tokenWithWrongUsername = jwtService.generateToken(wrongUsername);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = jwtService.validateToken(tokenWithWrongUsername, userDetails);

        assertFalse(isValid);
    }

}
