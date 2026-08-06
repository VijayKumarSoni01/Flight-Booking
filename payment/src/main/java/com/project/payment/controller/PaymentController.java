package com.project.payment.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.payment.dto.common.ApiResponse;
import com.project.payment.dto.request.CreatePaymentReqDTO;
import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.RetryPaymentReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.dto.response.PaymentStatusResDTO;
import com.project.payment.dto.response.PaymentSummaryResDTO;
import com.project.payment.dto.response.RefundResponseDTO;

import com.project.payment.service.interfaces.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/payments")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentConfirmationResDTO>> createPayment(
            @Valid @RequestBody CreatePaymentReqDTO request) {

        PaymentConfirmationResDTO response =
                paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PaymentConfirmationResDTO>builder()
                        .success(true)
                        .message("Payment created successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<PaymentStatusResDTO>> verifyPayment(
            @Valid @RequestBody VerifyPaymentReqDTO request) {

        PaymentStatusResDTO response =
                paymentService.verifyPayment(request);

        return ResponseEntity.ok(
                ApiResponse.<PaymentStatusResDTO>builder()
                        .success(true)
                        .message("Payment verified successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<RefundResponseDTO>> refundPayment(
            @Valid @RequestBody RefundPaymentReqDTO request) {

        RefundResponseDTO response =
                paymentService.refundPayment(request);

        return ResponseEntity.ok(
                ApiResponse.<RefundResponseDTO>builder()
                        .success(true)
                        .message("Refund processed successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/retry")
    public ResponseEntity<ApiResponse<PaymentConfirmationResDTO>> retryPayment(
            @Valid @RequestBody RetryPaymentReqDTO request) {

        PaymentConfirmationResDTO response =
                paymentService.retryPayment(request);

        return ResponseEntity.ok(
                ApiResponse.<PaymentConfirmationResDTO>builder()
                        .success(true)
                        .message("Payment retry initiated successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResDTO>> getPaymentById(
            @PathVariable Long paymentId) {

        PaymentResDTO response =
                paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(
                ApiResponse.<PaymentResDTO>builder()
                        .success(true)
                        .message("Payment retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/booking/{bookingReference}")
    public ResponseEntity<ApiResponse<List<PaymentSummaryResDTO>>> getPaymentsByBookingReference(
            @PathVariable String bookingReference) {

        List<PaymentSummaryResDTO> response =
                paymentService.getPaymentsByBookingReference(
                        bookingReference);

        return ResponseEntity.ok(
                ApiResponse.<List<PaymentSummaryResDTO>>builder()
                        .success(true)
                        .message("Payment history retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/status/{bookingReference}")
    public ResponseEntity<ApiResponse<PaymentStatusResDTO>> getPaymentStatus(
            @PathVariable String bookingReference) {

        PaymentStatusResDTO response =
                paymentService.getPaymentStatus(
                        bookingReference);

        return ResponseEntity.ok(
                ApiResponse.<PaymentStatusResDTO>builder()
                        .success(true)
                        .message("Payment status retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}