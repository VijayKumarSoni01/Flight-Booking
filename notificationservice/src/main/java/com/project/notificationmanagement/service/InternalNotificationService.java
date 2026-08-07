package com.project.notificationmanagement.service;


import com.project.notificationmanagement.dto.request.BookingCancellationEmailRequest;
import com.project.notificationmanagement.dto.request.BookingConfirmationEmailRequest;
import com.project.notificationmanagement.dto.request.PasswordResetEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentFailedEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentSuccessEmailRequest;
import com.project.notificationmanagement.dto.request.RefundEmailRequest;
import com.project.notificationmanagement.dto.response.NotificationResponse;


public interface InternalNotificationService {


    NotificationResponse sendBookingConfirmation(
            BookingConfirmationEmailRequest request);



    NotificationResponse sendBookingCancellation(
            BookingCancellationEmailRequest request);



    NotificationResponse sendPaymentSuccess(
            PaymentSuccessEmailRequest request);



    NotificationResponse sendPaymentFailed(
            PaymentFailedEmailRequest request);



    NotificationResponse sendRefund(
            RefundEmailRequest request);



    NotificationResponse sendPasswordReset(
            PasswordResetEmailRequest request);

}