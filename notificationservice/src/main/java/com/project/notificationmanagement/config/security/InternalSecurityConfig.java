package com.project.notificationmanagement.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class InternalSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



    @Bean
    @Order(1)
    public SecurityFilterChain internalSecurityFilterChain(
            HttpSecurity http) throws Exception {


        http

        .securityMatcher("/api/internal/**")


        .csrf(csrf -> csrf.disable())


        .sessionManagement(session ->
                session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                )
        )


        .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                        jwtAuthenticationEntryPoint
                )
        )


        .authorizeHttpRequests(auth -> auth


                .requestMatchers(
                        "/api/internal/notifications/test-save"
                )
                .permitAll()



                .requestMatchers(
                        "/api/internal/**"
                )
                .hasAnyRole(
                        "SERVICE",
                        "ADMIN"
                )
        )


        .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );


        return http.build();
    }

}