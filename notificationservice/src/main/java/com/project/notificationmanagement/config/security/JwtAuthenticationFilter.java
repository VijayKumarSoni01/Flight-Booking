package com.project.notificationmanagement.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtProperties.getHeader());

        System.out.println(
                "Authorization Header: " + authorizationHeader);

        System.out.println(
                "JWT Header Name: " + jwtProperties.getHeader());

        System.out.println(
                "JWT Prefix: " + jwtProperties.getPrefix());

        if (!StringUtils.hasText(authorizationHeader)
                || !authorizationHeader.startsWith(jwtProperties.getPrefix())) {

            System.out.println("JWT missing");

            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.resolveToken(authorizationHeader);

        System.out.println(
                "Token: " + token);

        boolean valid = jwtUtil.validateToken(token);

        System.out.println(
                "Token Valid: " + valid);

        if (!valid) {

            filterChain.doFilter(request, response);
            return;
        }

        CustomUserPrincipal principal = CustomUserPrincipal.builder()
                .userId(jwtUtil.extractUserId(token))
                .username(jwtUtil.extractUsername(token))
                .email(jwtUtil.extractEmail(token))
                .role(jwtUtil.extractRole(token))
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities());

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}