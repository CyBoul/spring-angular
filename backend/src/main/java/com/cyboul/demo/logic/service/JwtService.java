package com.cyboul.demo.logic.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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
        // clock.instant() = more deterministic for Tests
        Instant now = clock.instant();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(validityTimeMillis)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return Optional.ofNullable(extractClaims(token))
                .map(Claims::getSubject)
                .orElse("");
    }

    private Claims extractClaims(String token){
        Claims claims;
        try {
            claims = parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    private boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        Date expiration = claims.getExpiration();

        // clock.instant() = more deterministic for Tests
        return expiration == null
                || expiration.toInstant().isBefore(clock.instant());
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
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setClock(() -> Date.from(clock.instant()))
                .build();
    }
}
