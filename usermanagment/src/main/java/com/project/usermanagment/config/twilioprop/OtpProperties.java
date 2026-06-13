package com.project.usermanagment.config.twilioprop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {

    private int length;

    private int expiryMinutes;

    private int maxAttempts;
}
