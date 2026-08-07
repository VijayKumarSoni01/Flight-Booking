package com.project.notificationmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationManagementOpenAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                .info(new Info()
                        .title("Notification Management Service API")
                        .description("""
                                REST API documentation for the Notification Management Service.

                                Features:
                                - Send booking confirmation emails
                                - Send booking cancellation emails
                                - Send payment success emails
                                - Send payment failure emails
                                - Send refund emails
                                - Send custom emails
                                - Notification history
                                - Notification retry
                                - Notification statistics
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Flight Booking Team")
                                .email("support@flightbooking.com")
                                .url("https://flightbooking.com"))
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName))

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }
}