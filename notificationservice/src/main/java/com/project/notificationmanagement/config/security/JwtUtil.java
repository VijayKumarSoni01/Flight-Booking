package com.project.notificationmanagement.config.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey()
                        .getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {

        try {

            Claims claims = extractAllClaims(token);

            // Validate issuer
            if (!jwtProperties.getIssuer()
                    .equals(claims.getIssuer())) {

                return false;
            }

            // Validate audience
            if (claims.getAudience() == null
                    || !claims.getAudience()
                            .contains(jwtProperties.getAudience())) {

                return false;
            }

            return true;

        } catch (JwtException | IllegalArgumentException ex) {

            return false;
        }
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(
                        getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    public Long extractUserId(String token) {

        Object userId = extractAllClaims(token)
                .get("userId");

        if (userId == null) {

            return null;
        }

        return Long.valueOf(
                userId.toString());
    }

    public String extractEmail(String token) {

        Claims claims = extractAllClaims(token);

        Object email = claims.get("email");

        if (email != null) {

            return email.toString();
        }

        // fallback because your token uses subject as email
        return claims.getSubject();
    }

    public String extractRole(String token) {

        Claims claims = extractAllClaims(token);

        Object roles = claims.get("roles");

        if (roles instanceof java.util.List<?> roleList
                && !roleList.isEmpty()) {

            String role = roleList.get(0).toString();

            return role.replace(
                    "ROLE_",
                    "");
        }

        return null;
    }

    public String resolveToken(String authorizationHeader) {

        if (!StringUtils.hasText(authorizationHeader)) {

            return null;
        }

        if (!authorizationHeader
                .startsWith(jwtProperties.getPrefix())) {

            return null;
        }

        return authorizationHeader.substring(
                jwtProperties.getPrefix().length());
    }

}