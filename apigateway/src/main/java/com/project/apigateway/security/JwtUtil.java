package com.project.apigateway.security;


import java.util.Date;
import java.util.List;

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
                Keys.hmacShaKeyFor(
                        jwtProperties
                        .getSecretKey()
                        .getBytes()
                );

    }







    public Claims extractClaims(String token) {


        return Jwts.parser()

                .verifyWith(secretKey)

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }








    public String extractUsername(String token) {


        return extractClaims(token)
                .getSubject();

    }








    public Long extractUserId(String token) {


        Object userId =
                extractClaims(token)
                .get("userId");



        if(userId instanceof Integer id){

            return id.longValue();

        }


        if(userId instanceof Long id){

            return id;

        }


        return Long.parseLong(
                userId.toString()
        );

    }








    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {


        return extractClaims(token)
                .get(
                    "roles",
                    List.class
                );

    }








    public boolean isTokenExpired(String token) {


        Date expiry =
                extractClaims(token)
                .getExpiration();



        return expiry.before(
                new Date()
        );

    }








    public boolean validateToken(String token) {


        try {


            return !isTokenExpired(token);


        }
        catch(Exception e){


            return false;

        }

    }


}