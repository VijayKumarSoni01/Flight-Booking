package com.project.payment.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.payment.dto.common.ApiResponse;
import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.service.interfaces.PaymentAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@Validated
public class PaymentAdminController {

    private final PaymentAdminService paymentAdminService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PaymentResDTO>>> getAllPayments(
            Pageable pageable) {

        Page<PaymentResDTO> response =
                paymentAdminService.getAllPayments(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PaymentResDTO>>builder()
                        .success(true)
                        .message("Payments retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<ApiResponse<Page<PaymentResDTO>>> getPaymentsByStatus(
            @PathVariable PaymentStatus paymentStatus,
            Pageable pageable) {

        Page<PaymentResDTO> response =
                paymentAdminService.getPaymentsByStatus(
                        paymentStatus,
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PaymentResDTO>>builder()
                        .success(true)
                        .message("Payments retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/gateway/{paymentGateway}")
    public ResponseEntity<ApiResponse<Page<PaymentResDTO>>> getPaymentsByGateway(
            @PathVariable PaymentGateway paymentGateway,
            Pageable pageable) {

        Page<PaymentResDTO> response =
                paymentAdminService.getPaymentsByGateway(
                        paymentGateway,
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PaymentResDTO>>builder()
                        .success(true)
                        .message("Payments retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<PaymentResDTO>>> getPaymentsByUserId(
            @PathVariable Long userId,
            Pageable pageable) {

        Page<PaymentResDTO> response =
                paymentAdminService.getPaymentsByUserId(
                        userId,
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PaymentResDTO>>builder()
                        .success(true)
                        .message("Payments retrieved successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}