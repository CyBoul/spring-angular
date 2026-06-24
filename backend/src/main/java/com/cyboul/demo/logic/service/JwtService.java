package com.cyboul.demo.logic.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final long validityTimeMillis;
    private final SecretKey secretKey;
    private final JwtParser parser;
    private final Clock clock;

    public JwtService(
            @Value("${demo.jwt.secret}") String secret,
            @Value("${demo.jwt.expiration:3600000}") long validityTimeMillis,
            Clock clock
    ){
        validateSecret(secret);

        this.clock = clock;
        this.validityTimeMillis = validityTimeMillis;
        this.secretKey = buildSecretKey(secret);
        this.parser = buildParser(secretKey);
    }

    public String generateToken(String username) {
        Instant now = clock.instant();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(validityTimeMillis)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null
                && userDetails != null
                && username.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        try {
            return parser.parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    private void validateSecret(String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 characters");
        }
    }

    private SecretKey buildSecretKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private JwtParser buildParser(SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clock(() -> Date.from(clock.instant()))
                .build();
    }
}
