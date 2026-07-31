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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(exchange -> exchange

                        .pathMatchers("/api/public/**")
                        .permitAll()

                        .pathMatchers(
                                HttpMethod.GET,
                                "/api/flights/**",
                                "/api/aircrafts/**",
                                "/api/airlines/**",
                                "/api/airports/**",
                                "/api/baggage-policies/**",
                                "/api/flight-amenities/**",
                                "/api/flight-fares/**",
                                "/api/flight-status-info/**")
                        .permitAll()

                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/user/v3/api-docs",
                                "/flight/v3/api-docs")
                        .permitAll()

                        .anyExchange().authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }
}