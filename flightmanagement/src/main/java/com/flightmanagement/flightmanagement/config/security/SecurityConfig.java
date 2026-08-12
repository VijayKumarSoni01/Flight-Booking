package com.flightmanagement.flightmanagement.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.flightmanagement.flightmanagement.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(httpBasic -> httpBasic.disable())
                                .formLogin(form -> form.disable())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                .requestMatchers("/actuator/**").permitAll()
                                                .requestMatchers(
                                                                "/api/private/flights/seats/release/**",
                                                                "/api/private/flights/seats/confirm/**")
                                                .permitAll()

                                                // Search flights
                                                // Get flight details
                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/public/flights/**")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.GET, "/api/flights/**").permitAll()

                                                .requestMatchers(HttpMethod.POST, "/api/airlines/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/airlines/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/airlines/**").hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/api/airports/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/airports/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/airports/**").hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/api/aircrafts/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/aircrafts/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/aircrafts/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/api/flights/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/flights/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/flights/**").hasRole("ADMIN")

                                                .anyRequest().authenticated());

                http.addFilterBefore(
                                jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}