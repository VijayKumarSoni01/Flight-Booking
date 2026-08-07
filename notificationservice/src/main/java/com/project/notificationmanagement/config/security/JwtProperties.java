package com.project.notificationmanagement.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret is required")
    private String secretKey;

    @NotBlank(message = "JWT issuer is required")
    private String issuer;

    @NotBlank(message = "JWT audience is required")
    private String audience;

    @NotBlank(message = "Authorization header name is required")
    private String header = "Authorization";

    @NotBlank(message = "Token prefix is required")
    private String prefix = "Bearer ";
}