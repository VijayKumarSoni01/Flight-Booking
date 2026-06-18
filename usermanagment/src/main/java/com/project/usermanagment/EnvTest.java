package com.project.usermanagment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EnvTest {

    @Value("${BREVO_SMTP_HOST:NOT_FOUND}")
    private String host;

    @PostConstruct
    public void test() {
        System.out.println("SMTP HOST = " + host);
    }
}