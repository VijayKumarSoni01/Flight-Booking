package com.project.notificationmanagement.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public CustomUserPrincipal getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserPrincipal)) {
            return null;
        }

        return (CustomUserPrincipal) authentication.getPrincipal();
    }

    public Long getCurrentUserId() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getUserId() : null;
    }

    public String getCurrentUsername() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getUsername() : null;
    }

    public String getCurrentEmail() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getEmail() : null;
    }

    public String getCurrentRole() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getRole() : null;
    }

    public boolean isAuthenticated() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserPrincipal;
    }

    public boolean hasRole(String role) {

        String currentRole = getCurrentRole();

        return currentRole != null
                && currentRole.equalsIgnoreCase(role);
    }
}