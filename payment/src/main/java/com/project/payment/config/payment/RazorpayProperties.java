package com.project.payment.config.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.razorpay")
public class RazorpayProperties {

    @NotBlank(message = "Razorpay key id is required")
    private String keyId;

    @NotBlank(message = "Razorpay key secret is required")
    private String keySecret;

    private String webhookSecret;
}