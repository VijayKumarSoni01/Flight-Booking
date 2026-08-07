package com.project.notificationmanagement.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {


    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();


        JavaTimeModule module = new JavaTimeModule();


        module.addSerializer(
                LocalDateTime.class,
                new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
        );


        mapper.registerModule(module);


        return mapper;
    }

}