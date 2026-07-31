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
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n========== JWT FILTER START ==========");
        System.out.println("URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token found. Skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            System.out.println("STEP 1 : Token received");

            boolean valid = jwtUtil.isTokenValid(token);
            System.out.println("STEP 2 : Token valid = " + valid);

            if (valid) {

                System.out.println("STEP 3 : Extracting UserId...");
                Long userId = jwtUtil.extractUserId(token);
                System.out.println("UserId = " + userId);

                System.out.println("STEP 4 : Extracting Username...");
                String username = jwtUtil.extractUsername(token);
                System.out.println("Username = " + username);

                System.out.println("STEP 5 : Extracting Roles...");
                List<String> roles = jwtUtil.extractRoles(token);
                System.out.println("Roles = " + roles);

                System.out.println("STEP 6 : Creating Principal...");
                CustomUserPrincipal principal = new CustomUserPrincipal(userId, username, roles);

                System.out.println("Authorities = " + principal.getAuthorities());

                System.out.println("STEP 7 : Creating Authentication...");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principal,
                        token,
                        principal.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                System.out.println("STEP 8 : Setting SecurityContext...");
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("Authentication = "
                        + SecurityContextHolder.getContext().getAuthentication());

                System.out.println("========== JWT AUTH SUCCESS ==========");
            }

        } catch (Exception ex) {

            System.out.println("========== JWT AUTH FAILED ==========");
            ex.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

        System.out.println("========== JWT FILTER END ==========\n");
    }
}