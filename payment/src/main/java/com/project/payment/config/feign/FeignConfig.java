package com.project.payment.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final JwtFeignInterceptor jwtFeignInterceptor;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return jwtFeignInterceptor;
    }
}