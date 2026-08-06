package com.project.payment.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.payment.dto.common.MessageResponse;
import com.project.payment.service.interfaces.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<MessageResponse> handleRazorpayWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {

        webhookService.processRazorpayWebhook(
                payload,
                signature);

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message("Razorpay webhook processed successfully.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/stripe")
    public ResponseEntity<MessageResponse> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload) {

        webhookService.processStripeWebhook(
                payload,
                signature);

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message("Stripe webhook processed successfully.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}