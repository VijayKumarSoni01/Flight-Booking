package com.project.bookingmanagement.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.project.bookingmanagement.config.jwt.CustomUserPrincipal;

@Component
public class SecurityUtil {

    public Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {

            throw new IllegalStateException("Authenticated user not found.");
        }

        return principal.getUserId();
    }

    public String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {

            throw new IllegalStateException("Authenticated user not found.");
        }

        return principal.getUsername();
    }
}