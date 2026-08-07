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
public class VerificationEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String recipientEmail;

    @NotBlank(message = "Verification URL is required")
    private String verificationUrl;

    @NotNull(message = "Verification link expiry time is required")
    private LocalDateTime expiresAt;

    @NotBlank(message = "Application name is required")
    private String applicationName;

    @NotBlank(message = "Support email is required")
    @Email(message = "Invalid support email")
    private String supportEmail;
}