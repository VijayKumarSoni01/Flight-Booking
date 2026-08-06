package com.project.payment.config.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.stripe")
public class StripeProperties {

    @NotBlank(message = "Stripe secret key is required")
    private String secretKey;

    @NotBlank(message = "Stripe publishable key is required")
    private String publishableKey;

    @NotBlank(message = "Stripe webhook secret is required")
    private String webhookSecret;

}
