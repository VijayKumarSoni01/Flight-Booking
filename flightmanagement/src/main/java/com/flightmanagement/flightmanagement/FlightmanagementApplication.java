package com.flightmanagement.flightmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlightmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightmanagementApplication.class, args);
	}

}
