package com.project.notificationmanagement.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        http

        .csrf(csrf -> csrf.disable())


        .cors(Customizer.withDefaults())


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
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                )
                .permitAll()



                .requestMatchers(
                        "/actuator/**"
                )
                .permitAll()



                // Admin APIs
                .requestMatchers(
                        "/api/private/admin/**"
                )
                .hasRole("ADMIN")



                // User APIs
                .requestMatchers(
                        "/api/private/notifications/**"
                )
                .hasAnyRole(
                        "USER",
                        "ADMIN"
                )



                .anyRequest()
                .authenticated()
        )


        .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );


        return http.build();
    }

}