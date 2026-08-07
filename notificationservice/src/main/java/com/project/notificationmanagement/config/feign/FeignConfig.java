package com.project.notificationmanagement.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor requestInterceptor(
            JwtFeignInterceptor jwtFeignInterceptor) {

        return jwtFeignInterceptor;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

}