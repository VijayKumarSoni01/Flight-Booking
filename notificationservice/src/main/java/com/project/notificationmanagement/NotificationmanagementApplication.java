package com.project.notificationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.project.notificationmanagement.config.security.JwtProperties;


@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableMongoAuditing
@EnableConfigurationProperties(JwtProperties.class)
public class NotificationmanagementApplication {


	public static void main(String[] args) {

		SpringApplication.run(
				NotificationmanagementApplication.class,
				args);
	}

}