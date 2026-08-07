package com.project.notificationmanagement.service;

import com.project.notificationmanagement.dto.request.BookingCancellationEmailRequest;
import com.project.notificationmanagement.dto.request.BookingConfirmationEmailRequest;
import com.project.notificationmanagement.dto.request.PasswordResetEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentFailedEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentSuccessEmailRequest;
import com.project.notificationmanagement.dto.request.RefundEmailRequest;
import com.project.notificationmanagement.dto.request.SendEmailRequest;
import com.project.notificationmanagement.dto.response.EmailResponse;


public interface EmailService {


    EmailResponse sendEmail(
            SendEmailRequest request);



    EmailResponse sendBookingConfirmationEmail(
            BookingConfirmationEmailRequest request);



    EmailResponse sendBookingCancellationEmail(
            BookingCancellationEmailRequest request);



    EmailResponse sendPaymentSuccessEmail(
            PaymentSuccessEmailRequest request);



    EmailResponse sendPaymentFailedEmail(
            PaymentFailedEmailRequest request);



    EmailResponse sendRefundEmail(
            RefundEmailRequest request);



    EmailResponse sendPasswordResetEmail(
            PasswordResetEmailRequest request);



    EmailResponse sendTestEmail(
            String recipientEmail);

}