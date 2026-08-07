package com.project.notificationmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.project.notificationmanagement.config.mail.MailProperties;
import com.project.notificationmanagement.dto.request.BookingCancellationEmailRequest;
import com.project.notificationmanagement.dto.request.BookingConfirmationEmailRequest;
import com.project.notificationmanagement.dto.request.PasswordResetEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentFailedEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentSuccessEmailRequest;
import com.project.notificationmanagement.dto.request.RefundEmailRequest;
import com.project.notificationmanagement.dto.request.SendEmailRequest;
import com.project.notificationmanagement.dto.response.EmailResponse;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.exception.EmailSendingException;
import com.project.notificationmanagement.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

        private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

        private final JavaMailSender mailSender;

        private final MailProperties mailProperties;

        private final TemplateEngine templateEngine;

        @Override
        public EmailResponse sendEmail(
                        SendEmailRequest request) {

                LOGGER.info(
                                "Sending email. Recipient: {}, Subject: {}",
                                request.getRecipientEmail(),
                                request.getSubject());

                try {

                        MimeMessage message = mailSender.createMimeMessage();

                        MimeMessageHelper helper = new MimeMessageHelper(
                                        message,
                                        true,
                                        "UTF-8");

                        helper.setFrom(
                                        mailProperties.getFromEmail(),
                                        mailProperties.getFromName());

                        helper.setTo(
                                        request.getRecipientEmail());

                        helper.setSubject(
                                        request.getSubject());

                        helper.setText(
                                        request.getBody(),
                                        request.isHtml());

                        if (request.getCc() != null &&
                                        !request.getCc().isEmpty()) {

                                helper.setCc(
                                                request.getCc()
                                                                .toArray(new String[0]));

                        }

                        if (request.getBcc() != null &&
                                        !request.getBcc().isEmpty()) {

                                helper.setBcc(
                                                request.getBcc()
                                                                .toArray(new String[0]));

                        }

                        mailSender.send(message);

                        String messageId = UUID.randomUUID()
                                        .toString();

                        LOGGER.info(
                                        "Email sent successfully. ID: {}",
                                        messageId);

                        return EmailResponse.builder()

                                        .recipientName(
                                                        request.getRecipientName())

                                        .recipientEmail(
                                                        request.getRecipientEmail())

                                        .subject(
                                                        request.getSubject())

                                        .notificationStatus(
                                                        NotificationStatus.SENT)

                                        .providerMessageId(
                                                        messageId)

                                        .sentAt(
                                                        LocalDateTime.now())

                                        .message(
                                                        "Email sent successfully.")

                                        .build();

                } catch (Exception ex) {

                        LOGGER.error(
                                        "Email sending failed",
                                        ex);

                        throw new EmailSendingException(
                                        "Unable to send email",
                                        ex);

                }

        }

        @Override
        public EmailResponse sendBookingConfirmationEmail(
                        BookingConfirmationEmailRequest request) {

                LOGGER.info(
                                "Preparing booking confirmation email. BookingReference: {}",
                                request.getBookingReference());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "bookingReference",
                                request.getBookingReference());

                context.setVariable(
                                "pnr",
                                request.getPnr());

                context.setVariable(
                                "airlineName",
                                request.getAirlineName());

                context.setVariable(
                                "flightNumber",
                                request.getFlightNumber());

                context.setVariable(
                                "sourceAirport",
                                request.getSourceAirport());

                context.setVariable(
                                "destinationAirport",
                                request.getDestinationAirport());

                context.setVariable(
                                "departureTime",
                                request.getDepartureTime());

                context.setVariable(
                                "arrivalTime",
                                request.getArrivalTime());

                context.setVariable(
                                "seatNumbers",
                                request.getSeatNumbers());

                context.setVariable(
                                "passengerCount",
                                request.getPassengerCount());

                context.setVariable(
                                "cabinClass",
                                request.getCabinClass());

                context.setVariable(
                                "currency",
                                request.getCurrency());

                context.setVariable(
                                "totalFare",
                                request.getTotalFare());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                String htmlBody = processTemplate(
                                "booking-confirmation",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Booking Confirmation")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendBookingCancellationEmail(
                        BookingCancellationEmailRequest request) {

                LOGGER.info(
                                "Preparing booking cancellation email. BookingReference: {}",
                                request.getBookingReference());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "bookingReference",
                                request.getBookingReference());

                context.setVariable(
                                "pnr",
                                request.getPnr());

                context.setVariable(
                                "airlineName",
                                request.getAirlineName());

                context.setVariable(
                                "flightNumber",
                                request.getFlightNumber());

                context.setVariable(
                                "sourceAirport",
                                request.getSourceAirport());

                context.setVariable(
                                "destinationAirport",
                                request.getDestinationAirport());

                context.setVariable(
                                "departureTime",
                                request.getDepartureTime());

                context.setVariable(
                                "cancelledAt",
                                request.getCancelledAt());

                context.setVariable(
                                "cancellationReason",
                                request.getCancellationReason());

                context.setVariable(
                                "refundAmount",
                                request.getRefundAmount());

                context.setVariable(
                                "currency",
                                request.getCurrency());

                context.setVariable(
                                "refundStatus",
                                request.getRefundStatus());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                String htmlBody = processTemplate(
                                "booking-cancellation",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Booking Cancelled")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendPaymentSuccessEmail(
                        PaymentSuccessEmailRequest request) {

                LOGGER.info(
                                "Preparing payment success email. TransactionId: {}",
                                request.getTransactionId());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "paymentId",
                                request.getPaymentId());

                context.setVariable(
                                "transactionId",
                                request.getTransactionId());

                context.setVariable(
                                "paymentAmount",
                                request.getPaymentAmount());

                context.setVariable(
                                "currency",
                                request.getCurrency());

                context.setVariable(
                                "paymentMethod",
                                request.getPaymentMethod());

                context.setVariable(
                                "paymentGateway",
                                request.getPaymentGateway());

                context.setVariable(
                                "paymentStatus",
                                request.getPaymentStatus());

                context.setVariable(
                                "paidAt",
                                request.getPaidAt());

                context.setVariable(
                                "bookingReference",
                                request.getBookingReference());

                context.setVariable(
                                "pnr",
                                request.getPnr());

                context.setVariable(
                                "airlineName",
                                request.getAirlineName());

                context.setVariable(
                                "flightNumber",
                                request.getFlightNumber());

                context.setVariable(
                                "sourceAirport",
                                request.getSourceAirport());

                context.setVariable(
                                "destinationAirport",
                                request.getDestinationAirport());

                context.setVariable(
                                "seatNumbers",
                                request.getSeatNumbers());

                context.setVariable(
                                "cabinClass",
                                request.getCabinClass());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                String htmlBody = processTemplate(
                                "payment-success",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Payment Successful")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendPaymentFailedEmail(
                        PaymentFailedEmailRequest request) {

                LOGGER.warn(
                                "Preparing payment failed email. TransactionId: {}",
                                request.getTransactionId());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "bookingReference",
                                request.getBookingReference());

                context.setVariable(
                                "transactionId",
                                request.getTransactionId());

                context.setVariable(
                                "amount",
                                request.getAmount());

                context.setVariable(
                                "currency",
                                request.getCurrency());

                context.setVariable(
                                "paymentMethod",
                                request.getPaymentMethod());

                context.setVariable(
                                "paymentStatus",
                                request.getPaymentStatus());

                context.setVariable(
                                "failureReason",
                                request.getFailureReason());

                context.setVariable(
                                "retryPaymentUrl",
                                request.getRetryPaymentUrl());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                String htmlBody = processTemplate(
                                "payment-failed",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Payment Failed")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendRefundEmail(
                        RefundEmailRequest request) {

                LOGGER.info(
                                "Preparing refund email. RefundReference: {}",
                                request.getRefundReference());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "refundReference",
                                request.getRefundReference());

                context.setVariable(
                                "refundAmount",
                                request.getRefundAmount());

                context.setVariable(
                                "currency",
                                request.getCurrency());

                context.setVariable(
                                "refundStatus",
                                request.getRefundStatus());

                context.setVariable(
                                "refundReason",
                                request.getRefundReason());

                context.setVariable(
                                "bookingReference",
                                request.getBookingReference());

                context.setVariable(
                                "pnr",
                                request.getPnr());

                context.setVariable(
                                "flightNumber",
                                request.getFlightNumber());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                context.setVariable(
                                "logoPath",
                                mailProperties.getLogoPath());

                String htmlBody = processTemplate(
                                "refund-success",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Refund Processed")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendPasswordResetEmail(
                        PasswordResetEmailRequest request) {

                LOGGER.info(
                                "Preparing password reset email for user: {}",
                                request.getRecipientEmail());

                Context context = new Context();

                context.setVariable(
                                "recipientName",
                                request.getRecipientName());

                context.setVariable(
                                "resetUrl",
                                request.getResetUrl());

                context.setVariable(
                                "applicationName",
                                mailProperties.getApplicationName());

                context.setVariable(
                                "supportEmail",
                                mailProperties.getSupportEmail());

                context.setVariable(
                                "logoPath",
                                mailProperties.getLogoPath());

                String htmlBody = processTemplate(
                                "password-reset",
                                context);

                SendEmailRequest emailRequest = SendEmailRequest.builder()

                                .recipientName(
                                                request.getRecipientName())

                                .recipientEmail(
                                                request.getRecipientEmail())

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Password Reset")

                                .body(
                                                htmlBody)

                                .html(true)

                                .build();

                return sendEmail(emailRequest);
        }

        @Override
        public EmailResponse sendTestEmail(
                        String recipientEmail) {

                LOGGER.info(
                                "Sending test email to: {}",
                                recipientEmail);

                SendEmailRequest request = SendEmailRequest.builder()

                                .recipientName(
                                                "Test User")

                                .recipientEmail(
                                                recipientEmail)

                                .subject(
                                                mailProperties.getSubjectPrefix()
                                                                + " Test Email")

                                .body(
                                                """
                                                                <h2>SMTP Configuration Successful</h2>

                                                                <p>
                                                                Your notification service email configuration
                                                                is working correctly.
                                                                </p>
                                                                """)

                                .html(true)

                                .build();

                return sendEmail(request);
        }

        private String processTemplate(
                        String templateName,
                        Context context) {

                return templateEngine.process(
                                templateName,
                                context);
        }
}