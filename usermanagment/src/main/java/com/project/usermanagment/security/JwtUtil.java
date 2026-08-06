package com.project.usermanagment.security;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

// import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.usermanagment.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(UserDetails userDetails) {

    CustomUserDetails user = (CustomUserDetails) userDetails;

    Map<String, Object> claims = new HashMap<>();

    claims.put("userId", user.getId());

    claims.put("roles",
            user.getAuthorities()
                    .stream()
                    .map(authority -> authority.getAuthority())
                    .toList());

    return buildToken(
            claims,
            userDetails,
            jwtProperties.getExpirationTime());
}

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtProperties.getRefreshExpirationTime());
    }

    private String buildToken(Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expirationMs) {

        long now = System.currentTimeMillis();

        Date issued = new Date(now);
        Date expiry = new Date(now + expirationMs);

        ZonedDateTime istIssued = issued.toInstant().atZone(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime istExpiry = expiry.toInstant().atZone(ZoneId.of("Asia/Kolkata"));

        System.out.println("Issued (IST): " + istIssued);
        System.out.println("Expiry (IST): " + istExpiry);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issued)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractSubject(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractSubject(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, claims -> claims.getExpiration());
    }

    public <T> T extractClaim(String token, Function<? super Claims, ? extends T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}