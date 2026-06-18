package com.project.usermanagment.config.bravoprop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.mail")

public class MailProperty {

    private String fromEmail;

    private String fromName;

    private String baseUrl;

    private long verificationExpiryMinutes;

    public String buildVerificationUrl(String token) {
        return baseUrl + "/api/public/verify-email?token=" + token;
    }

}
