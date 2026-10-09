// package com.project.apigateway.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
// import org.springframework.security.config.web.server.ServerHttpSecurity;
// import org.springframework.security.web.server.SecurityWebFilterChain;

// import com.project.apigateway.filter.JwtAuthenticationFilter;

// @Configuration
// @EnableWebFluxSecurity
// public class SecurityConfig {

//         private final JwtAuthenticationFilter jwtAuthenticationFilter;

//         public SecurityConfig(
//                         JwtAuthenticationFilter jwtAuthenticationFilter) {
//                 this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//         }

//         @Bean
//         public SecurityWebFilterChain securityWebFilterChain(
//                         ServerHttpSecurity http) {

//                 return http
//                                 .csrf(csrf -> csrf.disable())

//                                 .cors(cors -> {
//                                 })

//                                 .authorizeExchange(exchange -> exchange

//                                                 .pathMatchers(
//                                                                 HttpMethod.OPTIONS,
//                                                                 "/**")
//                                                 .permitAll()

//                                                 .pathMatchers(
//                                                                 "/api/public/**")
//                                                 .permitAll()

//                                                 .pathMatchers(
//                                                                 "/swagger-ui/**",
//                                                                 "/v3/api-docs/**",
//                                                                 "/**/v3/api-docs")
//                                                 .permitAll()

//                                                 .anyExchange()
//                                                 .authenticated())

//                                 .addFilterAt(
//                                                 jwtAuthenticationFilter,
//                                                 SecurityWebFiltersOrder.AUTHENTICATION)

//                                 .build();
//         }
// }

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

                // Disable CSRF because this is a REST API Gateway
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeExchange(exchange -> exchange

                        // CORS preflight requests
                        .pathMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Public APIs
                        .pathMatchers(
                                "/api/public/**"
                        ).permitAll()

                        // Swagger
                        .pathMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/**/v3/api-docs"
                        ).permitAll()

                        // Everything else requires JWT
                        .anyExchange()
                        .authenticated()
                )

                // JWT authentication filter
                .addFilterAt(
                        jwtAuthenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }
}