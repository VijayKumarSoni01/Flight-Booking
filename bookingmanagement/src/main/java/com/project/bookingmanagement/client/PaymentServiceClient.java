package com.project.bookingmanagement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.bookingmanagement.config.feign.FeignConfig;
import com.project.bookingmanagement.dto.booking.request.RefundPaymentReqDTO;
import com.project.bookingmanagement.dto.booking.response.RefundResponseDTO;
import com.project.bookingmanagement.dto.common.ApiResponse;

@FeignClient(name = "payment-service", url = "${payment.service.url}", configuration = FeignConfig.class)
public interface PaymentServiceClient {

    @PostMapping("/api/private/payments/refund")
    ApiResponse<RefundResponseDTO> refundPayment(
            @RequestBody RefundPaymentReqDTO request);

}