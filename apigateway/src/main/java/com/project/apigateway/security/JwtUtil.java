package com.project.apigateway.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {

        this.secretKey =
                Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());

    }

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();

    }

    public boolean isTokenExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());

    }

    public boolean validateToken(String token) {

        try {

            return !isTokenExpired(token);

        } catch (Exception e) {

            return false;

        }

    }
}