package com.project.notificationmanagement.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String recipientEmail;

    @NotBlank(message = "Reset token is required")
    private String resetToken;

    @NotBlank(message = "Reset URL is required")
    private String resetUrl;

    @NotBlank(message = "Application name is required")
    private String applicationName;

    @NotBlank(message = "Support email is required")
    @Email(message = "Invalid support email")
    private String supportEmail;

    @NotNull(message = "Password reset request time is required")
    private LocalDateTime requestedAt;

    @NotNull(message = "Token expiry time is required")
    private LocalDateTime expiresAt;

    private String ipAddress;
}