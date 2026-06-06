package com.project.usermanagment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.usermanagment.config.JwtProperties;

@Component
public class TestRunner implements CommandLineRunner {

    private final JwtProperties jwtProperties;

    public TestRunner(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void run(String... args) {
        System.out.println("Secret Key: " + jwtProperties.getSecretKey());
        System.out.println("Expiration: " + jwtProperties.getExpirationTime());
        System.out.println("Refresh Expiration: " + jwtProperties.getRefreshExpirationTime());
    }
}