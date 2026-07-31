package com.project.apigateway.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secretKey;
    private long expirationTime;
    private long refreshExpirationTime;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }

    public void setRefreshExpirationTime(long refreshExpirationTime) {
        this.refreshExpirationTime = refreshExpirationTime;
    }
}