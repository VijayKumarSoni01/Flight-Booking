package com.project.notificationmanagement.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.notificationmanagement.config.feign.FeignConfig;
import com.project.notificationmanagement.dto.response.PaymentResponse;



@FeignClient(
        name = "payment-service",
        url = "${services.payment.url}",
        configuration = FeignConfig.class
)
public interface PaymentServiceClient {


    @GetMapping(
            "/api/internal/payments/{transactionId}"
    )
    PaymentResponse getPaymentByTransactionId(
            @PathVariable("transactionId") String transactionId);


}