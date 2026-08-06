package com.project.payment.config.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RazorpayConfig {

    private final RazorpayProperties razorpayProperties;

    @Bean
    public RazorpayClient razorpayClient() throws Exception {

        return new RazorpayClient(
                razorpayProperties.getKeyId(),
                razorpayProperties.getKeySecret());

    }

}
