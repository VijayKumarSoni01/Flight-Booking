package com.doisproject.userpreview.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "app.jwt")
@Component
@Data
public class JwtProperty {
    private String secret;
    private long expiration;
    private long refreshExpiration;
}
