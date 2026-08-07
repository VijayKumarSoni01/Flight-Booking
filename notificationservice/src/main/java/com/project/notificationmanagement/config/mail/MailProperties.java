package com.project.notificationmanagement.config.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    @NotBlank(message = "Sender email is required")
    @Email(message = "Invalid sender email")
    private String fromEmail;

    @NotBlank(message = "Sender name is required")
    private String fromName;

    @NotBlank(message = "Reply-to email is required")
    @Email(message = "Invalid reply-to email")
    private String replyTo;

    @NotBlank(message = "Application name is required")
    private String applicationName;

    @NotBlank(message = "Support email is required")
    @Email(message = "Invalid support email")
    private String supportEmail;

    @NotBlank(message = "Frontend URL is required")
    private String frontendUrl;

    @NotBlank(message = "Backend URL is required")
    private String backendUrl;

    @Min(value = 1, message = "Maximum retry count must be at least 1")
    private int maxRetryCount = 3;

    @Min(value = 1, message = "Retry interval must be at least 1 minute")
    private int retryIntervalMinutes = 10;

    @NotBlank(message = "Logo path is required")
    private String logoPath;

    @NotBlank(message = "Banner path is required")
    private String bannerPath;

    private boolean htmlEnabled = true;

    @NotBlank(message = "Subject prefix is required")
    private String subjectPrefix = "[Flight Booking]";
}