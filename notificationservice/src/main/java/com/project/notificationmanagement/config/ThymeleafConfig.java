package com.project.notificationmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Configuration
public class ThymeleafConfig {


    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {


        ClassLoaderTemplateResolver resolver =
                new ClassLoaderTemplateResolver();


        resolver.setPrefix(
                "templates/");


        resolver.setSuffix(
                ".html");


        resolver.setTemplateMode(
                "HTML");


        resolver.setCharacterEncoding(
                "UTF-8");


        resolver.setCacheable(
                false);


        return resolver;
    }

}