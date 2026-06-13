package com.project.usermanagment.config.twilioprop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "twilio")
@Component
@Data
public class TwilioProperties {

    private String accountSid;
    private String authToken;
    private String phoneNumber;
}
