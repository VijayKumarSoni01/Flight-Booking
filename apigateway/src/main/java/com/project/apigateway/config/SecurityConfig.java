package com.project.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;

import com.project.apigateway.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter) {

                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(
                        ServerHttpSecurity http) {

                return http

                                .csrf(csrf -> csrf.disable())

                                .cors(cors -> {
                                })

                                .authorizeExchange(exchange -> exchange

                                                // Browser preflight
                                                .pathMatchers(
                                                                HttpMethod.OPTIONS,
                                                                "/**")
                                                .permitAll()

                                                // Public APIs
                                                .pathMatchers(
                                                                "/api/public/**")
                                                .permitAll()

                                                // Swagger
                                                .pathMatchers(
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/**/v3/api-docs")
                                                .permitAll()

                                                // Protected APIs
                                                .anyExchange()
                                                .authenticated()

                                )

                                // IMPORTANT
                                // JWT verification happens here
                                .addFilterAt(
                                                jwtAuthenticationFilter,
                                                SecurityWebFiltersOrder.AUTHENTICATION)

                                .build();

        }

}