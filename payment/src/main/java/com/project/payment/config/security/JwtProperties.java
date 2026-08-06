package com.project.payment.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret key is required")
    private String secretKey;

    @Positive(message = "JWT expiration time must be greater than zero")
    private Long expirationTime;

    @Positive(message = "JWT refresh expiration time must be greater than zero")
    private Long refreshExpirationTime;

}