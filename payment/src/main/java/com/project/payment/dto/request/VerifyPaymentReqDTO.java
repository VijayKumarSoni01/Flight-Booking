package com.project.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyPaymentReqDTO {

    @NotBlank(message = "Gateway order id is required")
    private String gatewayOrderId;

    @NotBlank(message = "Gateway payment id is required")
    private String gatewayPaymentId;

    private String transactionId;

    private String gatewayResponse;

    private String signature;

    @Size(max = 500)
    private String webhookEventId;
}
