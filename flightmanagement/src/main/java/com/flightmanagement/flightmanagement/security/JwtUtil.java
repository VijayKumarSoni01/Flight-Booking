package com.flightmanagement.flightmanagement.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.flightmanagement.flightmanagement.config.security.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
    return extractClaim(token, claims -> claims.getSubject());
}

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        return extractClaim(token,
                claims -> claims.get("roles", List.class));
    }

    public Date extractExpiration(String token) {
    return extractClaim(token, claims -> claims.getExpiration());
}

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token) {

        try {

            return !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException ex) {

            return false;
        }
    }

}
