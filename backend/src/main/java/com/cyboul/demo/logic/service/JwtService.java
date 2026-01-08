package com.cyboul.demo.logic.service;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {

    private final long expiration;
    private final SecretKey secretKey;
    private final JwtParser parser;
    private final Clock clock;

    public JwtService(
            @Value("${demo.jwt.secret}") String secret,
            @Value("${demo.jwt.expiration:3600000}") long expiration,
            Clock clock
    ){
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 characters");
        }
        this.expiration = expiration;

        this.clock = clock;
        // secret is base64 encoded
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.parser = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build();
    }

    public String generateToken(String username) {
        // clock.instant() = more deterministic for Tests
        // new Date() replaced by Date.From(fixedInstant)
        Instant now = clock.instant();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expiration)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return this.parser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = this.parser
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        // clock.instant() = more deterministic for Tests
        // new Date() comparison replaced by fixed instant comparison
        return expiration.toInstant().isBefore(clock.instant());
    }
}
