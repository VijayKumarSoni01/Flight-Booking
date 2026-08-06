package com.project.payment.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserPrincipal getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {

            return null;
        }

        return principal;
    }

    public static Long getCurrentUserId() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getUserId() : null;
    }

    public static String getCurrentUsername() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getUsername() : null;
    }

    public static String getCurrentEmail() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getEmail() : null;
    }

    public static String getCurrentRole() {

        CustomUserPrincipal principal = getCurrentUser();

        return principal != null ? principal.getRole() : null;
    }

    public static boolean hasRole(String role) {

        String currentRole = getCurrentRole();

        return currentRole != null
                && currentRole.equalsIgnoreCase(role);
    }

    public static boolean isAdmin() {

        return hasRole("ADMIN");
    }

    public static boolean isUser() {

        return hasRole("USER");
    }

}