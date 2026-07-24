// package com.flightmanagement.flightmanagement.config.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.flightmanagement.flightmanagement.security.JwtAuthenticationFilter;

// import lombok.RequiredArgsConstructor;

// @Configuration
// @EnableMethodSecurity
// @RequiredArgsConstructor
// public class SecurityConfig {

//     private final JwtAuthenticationFilter jwtAuthenticationFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http)
//             throws Exception {

//         http
//                 .csrf(csrf -> csrf.disable())

//                 .sessionManagement(session -> session
//                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                 .addFilterBefore(
//                         jwtAuthenticationFilter,
//                         UsernamePasswordAuthenticationFilter.class)

//                 .authorizeHttpRequests(auth -> auth

//                         // Swagger
//                         .requestMatchers(
//                                 "/swagger-ui/**",
//                                 "/v3/api-docs/**")
//                         .permitAll()

//                         // Airports
//                         .requestMatchers(HttpMethod.GET, "/api/airports/**")
//                         .permitAll()

//                         .requestMatchers(HttpMethod.POST, "/api/airports/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.PUT, "/api/airports/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.DELETE, "/api/airports/**")
//                         .hasRole("ADMIN")

//                         // Airlines
//                         .requestMatchers(HttpMethod.GET, "/api/airlines/**")
//                         .permitAll()

//                         .requestMatchers(HttpMethod.POST, "/api/airlines/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.PUT, "/api/airlines/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.DELETE, "/api/airlines/**")
//                         .hasRole("ADMIN")

//                         // Aircraft
//                         .requestMatchers(HttpMethod.GET, "/api/aircraft/**")
//                         .permitAll()

//                         .requestMatchers(HttpMethod.POST, "/api/aircraft/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.PUT, "/api/aircraft/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.DELETE, "/api/aircraft/**")
//                         .hasRole("ADMIN")

//                         // Flights
//                         .requestMatchers(HttpMethod.GET, "/api/flights/**")
//                         .permitAll()

//                         .requestMatchers(HttpMethod.POST, "/api/flights/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.PUT, "/api/flights/**")
//                         .hasRole("ADMIN")

//                         .requestMatchers(HttpMethod.DELETE, "/api/flights/**")
//                         .hasRole("ADMIN")

//                         .anyRequest()
//                         .authenticated());

//         return http.build();
//     }
// }

package com.flightmanagement.flightmanagement.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll());

        return http.build();
    }
}