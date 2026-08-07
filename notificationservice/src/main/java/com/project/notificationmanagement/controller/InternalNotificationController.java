package com.project.notificationmanagement.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.notificationmanagement.dto.common.ApiResponse;
import com.project.notificationmanagement.dto.request.BookingCancellationEmailRequest;
import com.project.notificationmanagement.dto.request.BookingConfirmationEmailRequest;
import com.project.notificationmanagement.dto.request.PasswordResetEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentFailedEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentSuccessEmailRequest;
import com.project.notificationmanagement.dto.request.RefundEmailRequest;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.service.InternalNotificationService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/internal/notifications")
@PreAuthorize("hasAnyRole('SERVICE','ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class InternalNotificationController {



    private final InternalNotificationService internalNotificationService;





    @Operation(
            summary = "Send booking confirmation notification")
    @PostMapping("/booking-confirmation")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendBookingConfirmation(
            @RequestBody BookingConfirmationEmailRequest request) {


        log.info(
                "Sending booking confirmation. BookingReference: {}",
                request.getBookingReference());


        NotificationResponse response =
                internalNotificationService
                .sendBookingConfirmation(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking confirmation notification sent successfully."
                )
        );
    }






    @Operation(
            summary = "Send booking cancellation notification")
    @PostMapping("/booking-cancellation")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendBookingCancellation(
            @RequestBody BookingCancellationEmailRequest request) {


        NotificationResponse response =
                internalNotificationService
                .sendBookingCancellation(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking cancellation notification sent successfully."
                )
        );
    }







    @Operation(
            summary = "Send payment success notification")
    @PostMapping("/payment-success")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendPaymentSuccess(
            @RequestBody PaymentSuccessEmailRequest request) {


        NotificationResponse response =
                internalNotificationService
                .sendPaymentSuccess(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Payment success notification sent successfully."
                )
        );
    }







    @Operation(
            summary = "Send payment failed notification")
    @PostMapping("/payment-failed")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendPaymentFailed(
            @RequestBody PaymentFailedEmailRequest request) {


        NotificationResponse response =
                internalNotificationService
                .sendPaymentFailed(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Payment failed notification sent successfully."
                )
        );
    }







    @Operation(
            summary = "Send refund notification")
    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendRefund(
            @RequestBody RefundEmailRequest request) {


        NotificationResponse response =
                internalNotificationService
                .sendRefund(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Refund notification sent successfully."
                )
        );
    }







    @Operation(
            summary = "Send password reset notification")
    @PostMapping("/password-reset")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendPasswordReset(
            @RequestBody PasswordResetEmailRequest request) {


        NotificationResponse response =
                internalNotificationService
                .sendPasswordReset(request);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Password reset notification sent successfully."
                )
        );
    }

}