package com.project.payment.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        private final JwtAuthenticationEntryPoint authenticationEntryPoint;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http

                                .csrf(csrf -> csrf.disable())

                                // remove CORS handling from microservice
                                // Gateway will handle it
                                .cors(cors -> {
                                })

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .exceptionHandling(exception -> exception.authenticationEntryPoint(
                                                authenticationEntryPoint))

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/v3/api-docs/**",
                                                                "/actuator/health")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/api/public/**")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/api/admin/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                "/api/private/**")
                                                .hasAnyRole(
                                                                "USER",
                                                                "ADMIN")

                                                .anyRequest()
                                                .authenticated()

                                )

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

}