package com.project.notificationmanagement.service.impl;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.project.notificationmanagement.config.mail.MailProperties;
import com.project.notificationmanagement.dto.request.BookingCancellationEmailRequest;
import com.project.notificationmanagement.dto.request.BookingConfirmationEmailRequest;
import com.project.notificationmanagement.dto.request.PasswordResetEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentFailedEmailRequest;
import com.project.notificationmanagement.dto.request.PaymentSuccessEmailRequest;
import com.project.notificationmanagement.dto.request.RefundEmailRequest;
import com.project.notificationmanagement.dto.response.EmailResponse;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.entity.Notification;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;
import com.project.notificationmanagement.mapper.NotificationMapper;
import com.project.notificationmanagement.repository.NotificationRepository;
import com.project.notificationmanagement.service.EmailService;
import com.project.notificationmanagement.service.InternalNotificationService;


import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class InternalNotificationServiceImpl 
        implements InternalNotificationService {



    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    InternalNotificationServiceImpl.class);



    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final EmailService emailService;

    private final MailProperties mailProperties;





    @Override
    @Transactional
    public NotificationResponse sendBookingConfirmation(
            BookingConfirmationEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.BOOKING_CONFIRMATION,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        request.getBookingReference(),
                        request.getPnr(),
                        "Booking Confirmation"
                );


        return processEmail(
                notification,
                () -> emailService.sendBookingConfirmationEmail(request)
        );
    }







    @Override
    @Transactional
    public NotificationResponse sendBookingCancellation(
            BookingCancellationEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.BOOKING_CANCELLATION,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        request.getBookingReference(),
                        request.getPnr(),
                        "Booking Cancellation"
                );


        return processEmail(
                notification,
                () -> emailService.sendBookingCancellationEmail(request)
        );
    }







    @Override
    @Transactional
    public NotificationResponse sendPaymentSuccess(
            PaymentSuccessEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.PAYMENT_SUCCESS,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        request.getBookingReference(),
                        request.getPnr(),
                        "Payment Successful"
                );


        return processEmail(
                notification,
                () -> emailService.sendPaymentSuccessEmail(request)
        );
    }







    @Override
    @Transactional
    public NotificationResponse sendPaymentFailed(
            PaymentFailedEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.PAYMENT_FAILED,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        request.getBookingReference(),
                        null,
                        "Payment Failed"
                );


        return processEmail(
                notification,
                () -> emailService.sendPaymentFailedEmail(request)
        );
    }







    @Override
    @Transactional
    public NotificationResponse sendRefund(
            RefundEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.REFUND_SUCCESS,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        request.getBookingReference(),
                        request.getPnr(),
                        "Refund Processed"
                );


        return processEmail(
                notification,
                () -> emailService.sendRefundEmail(request)
        );
    }







    @Override
    @Transactional
    public NotificationResponse sendPasswordReset(
            PasswordResetEmailRequest request) {


        Notification notification =
                createNotification(
                        NotificationType.PASSWORD_RESET,
                        request.getUserId(),
                        request.getRecipientName(),
                        request.getRecipientEmail(),
                        null,
                        null,
                        "Password Reset"
                );


        return processEmail(
                notification,
                () -> emailService.sendPasswordResetEmail(request)
        );
    }







    private NotificationResponse processEmail(
            Notification notification,
            EmailSender sender) {


        notificationRepository.save(notification);


        try {


            notification.setNotificationStatus(
                    NotificationStatus.PROCESSING);



            EmailResponse response =
                    sender.send();



            notification.setNotificationStatus(
                    NotificationStatus.SENT);



            notification.setProviderMessageId(
                    response.getProviderMessageId());



            notification.setSentAt(
                    LocalDateTime.now());



            notification.setFailureReason(null);



        }
        catch(Exception ex){


            handleFailure(
                    notification,
                    ex);
        }



        return notificationMapper
                .toNotificationResponse(
                        notificationRepository.save(notification));
    }







    private Notification createNotification(
            NotificationType type,
            Long userId,
            String name,
            String email,
            String bookingReference,
            String pnr,
            String subject) {


        return Notification.builder()

                .notificationType(type)

                .notificationStatus(
                        NotificationStatus.PENDING)

                .userId(userId)

                .recipientName(name)

                .recipientEmail(email)

                .bookingReference(bookingReference)

                .pnr(pnr)

                .subject(subject)

                .retryCount(0)

                .build();
    }







    private void handleFailure(
            Notification notification,
            Exception ex){


        LOGGER.error(
                "Notification failed : {}",
                notification.getId(),
                ex);



        int retry =
                notification.getRetryCount()+1;



        notification.setRetryCount(retry);


        notification.setFailureReason(
                ex.getMessage());



        notification.setLastRetryAt(
                LocalDateTime.now());



        if(retry >= mailProperties.getMaxRetryCount()){


            notification.setNotificationStatus(
                    NotificationStatus.FAILED);



            notification.setNextRetryAt(null);



        }
        else{


            notification.setNotificationStatus(
                    NotificationStatus.PENDING);



            notification.setNextRetryAt(
                    LocalDateTime.now()
                    .plusMinutes(
                       mailProperties.getRetryIntervalMinutes()
                    ));
        }
    }






    @FunctionalInterface
    private interface EmailSender {

        EmailResponse send();

    }

}