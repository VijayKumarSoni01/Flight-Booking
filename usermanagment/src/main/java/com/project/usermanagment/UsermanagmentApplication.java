package com.project.usermanagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Date;  

@SpringBootApplication
@ConfigurationPropertiesScan
public class UsermanagmentApplication {

	public static void main(String[] args) {

		System.out.println("Server Time: " + new Date());
        System.out.println("Zone: " + java.time.ZoneId.systemDefault());
    	SpringApplication.run(UsermanagmentApplication.class, args);
	}

}
