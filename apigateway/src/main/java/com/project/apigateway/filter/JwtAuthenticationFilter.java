package com.project.apigateway.filter;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.project.apigateway.security.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

        private final JwtUtil jwtUtil;

        public JwtAuthenticationFilter(
                        JwtUtil jwtUtil) {

                this.jwtUtil = jwtUtil;

        }

        @Override
        public Mono<Void> filter(
                        ServerWebExchange exchange,
                        WebFilterChain chain) {

                String path = exchange.getRequest()
                                .getURI()
                                .getPath();

                System.out.println(
                                "========== GATEWAY JWT FILTER ==========");

                System.out.println(
                                "REQUEST : "
                                                + exchange.getRequest().getMethod()
                                                + " "
                                                + path);

                // Allow CORS preflight

                if (exchange.getRequest()
                                .getMethod()
                                .equals(HttpMethod.OPTIONS)) {

                        return chain.filter(exchange);
                }

                // Public APIs

                if (path.startsWith("/api/public")
                                ||
                                path.startsWith("/swagger")
                                ||
                                path.startsWith("/v3/api-docs")) {

                        return chain.filter(exchange);

                }

                String authHeader = exchange.getRequest()
                                .getHeaders()
                                .getFirst(
                                                HttpHeaders.AUTHORIZATION);

                System.out.println(
                                "AUTH HEADER : "
                                                + authHeader);

                // No token

                if (authHeader == null
                                ||
                                !authHeader.startsWith("Bearer ")) {

                        exchange.getResponse()
                                        .setStatusCode(
                                                        HttpStatus.UNAUTHORIZED);

                        return exchange.getResponse()
                                        .setComplete();

                }

                String token = authHeader.substring(7);

                try {

                        boolean valid = jwtUtil.validateToken(token);

                        System.out.println(
                                        "TOKEN VALID : "
                                                        + valid);

                        if (!valid) {

                                exchange.getResponse()
                                                .setStatusCode(
                                                                HttpStatus.UNAUTHORIZED);

                                return exchange.getResponse()
                                                .setComplete();

                        }

                        String username = jwtUtil.extractUsername(token);

                        List<String> roles = jwtUtil.extractRoles(token);

                        System.out.println(
                                        "USERNAME : "
                                                        + username);

                        System.out.println(
                                        "ROLES : "
                                                        + roles);

                        List<SimpleGrantedAuthority> authorities =

                                        roles.stream()

                                                        .map(role -> new SimpleGrantedAuthority(
                                                                        role.startsWith("ROLE_")
                                                                                        ? role
                                                                                        : "ROLE_" + role))

                                                        .toList();

                        UsernamePasswordAuthenticationToken authentication =

                                        new UsernamePasswordAuthenticationToken(

                                                        username,

                                                        null,

                                                        authorities

                                        );

                        System.out.println(
                                        "JWT AUTH SUCCESS");

                        return chain.filter(exchange)

                                        .contextWrite(

                                                        ReactiveSecurityContextHolder
                                                                        .withAuthentication(authentication)

                                        );

                } catch (Exception e) {

                        System.out.println(
                                        "JWT ERROR : "
                                                        + e.getMessage());

                        exchange.getResponse()
                                        .setStatusCode(
                                                        HttpStatus.UNAUTHORIZED);

                        return exchange.getResponse()
                                        .setComplete();

                }

        }

}