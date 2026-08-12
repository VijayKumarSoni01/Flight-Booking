package com.project.bookingmanagement.config.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

        @Override
        protected void doFilterInternal(

                        HttpServletRequest request,

                        HttpServletResponse response,

                        FilterChain filterChain

        ) throws ServletException, IOException {

                // Skip CORS preflight
                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

                        filterChain.doFilter(
                                        request,
                                        response);

                        return;
                }

                System.out.println(
                                "\n========== JWT FILTER START ==========");

                System.out.println(
                                "URI : "
                                                + request.getRequestURI());

                String authHeader = request.getHeader("Authorization");

                System.out.println(
                                "Authorization Header : "
                                                + authHeader);

                // No JWT
                if (authHeader == null ||
                                !authHeader.startsWith("Bearer ")) {

                        System.out.println(
                                        "No Bearer token found");

                        filterChain.doFilter(
                                        request,
                                        response);

                        return;
                }

                String token = authHeader.substring(7);

                try {

                        boolean valid = jwtUtil.isTokenValid(token);

                        System.out.println(
                                        "Token valid : "
                                                        + valid);

                        if (valid && SecurityContextHolder
                                        .getContext()
                                        .getAuthentication() == null) {

                                Long userId = jwtUtil.extractUserId(token);

                                String username = jwtUtil.extractUsername(token);

                                List<String> roles = jwtUtil.extractRoles(token);

                                CustomUserPrincipal principal = new CustomUserPrincipal(
                                                userId,
                                                username,
                                                roles);

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(

                                                principal,

                                                token,

                                                principal.getAuthorities()

                                );

                                authentication.setDetails(

                                                new WebAuthenticationDetailsSource()
                                                                .buildDetails(request)

                                );

                                SecurityContextHolder
                                                .getContext()
                                                .setAuthentication(authentication);

                                System.out.println(
                                                "JWT AUTH SUCCESS");

                        }

                } catch (Exception ex) {

                        System.out.println(
                                        "JWT AUTH FAILED");

                        ex.printStackTrace();

                        SecurityContextHolder
                                        .clearContext();

                }

                filterChain.doFilter(
                                request,
                                response);

                System.out.println(
                                "========== JWT FILTER END ==========\n");

        }

}