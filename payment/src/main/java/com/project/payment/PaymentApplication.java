package com.project.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.project.payment.config.payment.RazorpayProperties;
import com.project.payment.config.payment.StripeProperties;
import com.project.payment.config.security.JwtProperties;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({
		JwtProperties.class,
		StripeProperties.class,
		RazorpayProperties.class
})
public class PaymentApplication {

	public static void main(String[] args) {

		System.out.println("KEY = " + System.getenv("RAZORPAY_KEY_ID"));

		SpringApplication.run(
				PaymentApplication.class,
				args);

	}

}