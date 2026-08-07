package com.project.notificationmanagement.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.notificationmanagement.config.mail.MailProperties;
import com.project.notificationmanagement.dto.request.SendEmailRequest;
import com.project.notificationmanagement.dto.response.EmailResponse;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.entity.Notification;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.exception.NotificationNotFoundException;
import com.project.notificationmanagement.mapper.NotificationMapper;
import com.project.notificationmanagement.repository.NotificationRepository;
import com.project.notificationmanagement.service.EmailService;
import com.project.notificationmanagement.service.RetryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetryServiceImpl implements RetryService {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(RetryServiceImpl.class);



    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final EmailService emailService;

    private final MailProperties mailProperties;




    @Override
    @Transactional
    public NotificationResponse retryNotification(
            String notificationId) {


        LOGGER.info(
                "Manual retry started. NotificationId: {}",
                notificationId);



        Notification notification =
                findNotification(notificationId);



        validateRetry(notification);



        return retry(notification);
    }





    @Override
    @Transactional
    public void processRetryNotifications() {


        LOGGER.info(
                "Starting scheduled notification retry process");



        var notifications =
                notificationRepository
                        .findByNotificationStatusAndNextRetryAtBefore(
                                NotificationStatus.PENDING,
                                LocalDateTime.now());



        LOGGER.info(
                "Retry candidates found: {}",
                notifications.size());



        for(Notification notification : notifications) {


            try {

                retry(notification);


            } catch(Exception ex) {


                LOGGER.error(
                        "Retry process failed. NotificationId: {}",
                        notification.getId(),
                        ex);
            }
        }
    }





    @Transactional
    protected NotificationResponse retry(
            Notification notification) {


        try {


            notification.setNotificationStatus(
                    NotificationStatus.PROCESSING);



            notificationRepository.save(notification);



            SendEmailRequest request =
                    SendEmailRequest.builder()

                    .recipientName(
                            notification.getRecipientName())

                    .recipientEmail(
                            notification.getRecipientEmail())

                    .subject(
                            notification.getSubject())

                    .body(
                            notification.getBody())

                    .html(
                            mailProperties.isHtmlEnabled())

                    .build();




            EmailResponse response =
                    emailService.sendEmail(request);




            notification.setNotificationStatus(
                    NotificationStatus.SENT);



            notification.setProviderMessageId(
                    response.getProviderMessageId());



            notification.setSentAt(
                    LocalDateTime.now());



            notification.setNextRetryAt(null);



            notification.setFailureReason(null);



            LOGGER.info(
                    "Notification retry successful. NotificationId: {}",
                    notification.getId());



        }
        catch(Exception ex) {


            LOGGER.error(
                    "Notification retry failed. NotificationId: {}",
                    notification.getId(),
                    ex);



            handleRetryFailure(
                    notification,
                    ex);
        }



        return notificationMapper
                .toNotificationResponse(
                        notificationRepository.save(notification));
    }





    private void handleRetryFailure(
            Notification notification,
            Exception ex) {



        int retryCount =
                notification.getRetryCount() + 1;



        notification.setRetryCount(
                retryCount);



        notification.setFailureReason(
                ex.getMessage());



        notification.setLastRetryAt(
                LocalDateTime.now());




        if(retryCount >=
                mailProperties.getMaxRetryCount()) {



            notification.setNotificationStatus(
                    NotificationStatus.FAILED);



            notification.setNextRetryAt(null);



            LOGGER.error(
                    "Retry limit reached. NotificationId: {}",
                    notification.getId());



        } else {


            notification.setNotificationStatus(
                    NotificationStatus.PENDING);



            notification.setNextRetryAt(
                    LocalDateTime.now()
                    .plusMinutes(
                            mailProperties
                            .getRetryIntervalMinutes()));



            LOGGER.warn(
                    "Notification scheduled for retry. NotificationId: {}, RetryCount: {}",
                    notification.getId(),
                    retryCount);
        }
    }







    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getFailedNotifications(
            Pageable pageable) {


        return notificationRepository
                .findByNotificationStatus(
                        NotificationStatus.FAILED,
                        pageable)

                .map(notificationMapper::toNotificationResponse);
    }





    @Override
    @Transactional(readOnly = true)
    public long getRetryCount(
            String notificationId) {


        return findNotification(notificationId)
                .getRetryCount();
    }





    @Override
    @Transactional(readOnly = true)
    public boolean canRetry(
            String notificationId) {


        Notification notification =
                findNotification(notificationId);



        return notification.getRetryCount()
                < mailProperties.getMaxRetryCount();
    }





    @Override
    @Transactional(readOnly = true)
    public LocalDateTime getNextRetryTime(
            String notificationId) {


        return findNotification(notificationId)
                .getNextRetryAt();
    }





    @Override
    @Transactional
    public void cancelRetry(
            String notificationId) {


        Notification notification =
                findNotification(notificationId);



        notification.setNotificationStatus(
                NotificationStatus.FAILED);



        notification.setNextRetryAt(null);



        notificationRepository.save(notification);



        LOGGER.info(
                "Retry cancelled. NotificationId: {}",
                notificationId);
    }





    @Override
    @Transactional
    public void resetRetryCount(
            String notificationId) {


        Notification notification =
                findNotification(notificationId);



        notification.setRetryCount(0);



        notification.setNotificationStatus(
                NotificationStatus.PENDING);



        notification.setNextRetryAt(
                LocalDateTime.now());



        notification.setFailureReason(null);



        notificationRepository.save(notification);



        LOGGER.info(
                "Retry count reset. NotificationId: {}",
                notificationId);
    }





    private void validateRetry(
            Notification notification) {


        if(notification.getNotificationStatus()
                == NotificationStatus.SENT) {


            throw new IllegalStateException(
                    "Notification already sent");
        }



        if(notification.getRetryCount()
                >= mailProperties.getMaxRetryCount()) {


            throw new IllegalStateException(
                    "Retry limit reached");
        }
    }





    private Notification findNotification(
            String notificationId) {


        return notificationRepository
                .findById(notificationId)

                .orElseThrow(
                        () -> new NotificationNotFoundException(
                                "Notification not found with id: "
                                + notificationId));
    }
}