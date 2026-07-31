package com.project.bookingmanagement.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Authentication getAuthentication() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException("No authenticated user found.");
        }

        return authentication;
    }

    public String getUsername() {
        return getAuthentication().getName();
    }

    public Long getCurrentUserId() {

        try {
            return Long.parseLong(getAuthentication().getName());
        } catch (NumberFormatException ex) {
            throw new IllegalStateException(
                    "Authentication principal is not a numeric user ID.");
        }
    }

    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}