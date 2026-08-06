package com.project.payment.dto.request;

import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentReqDTO {

    @NotBlank(message = "Booking reference is required")
    private String bookingReference;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment gateway is required")
    private PaymentGateway paymentGateway;

    private String description;
}