package com.project.notificationmanagement.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.notificationmanagement.service.RetryService;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class NotificationRetryScheduler {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificationRetryScheduler.class);



    private final RetryService retryService;


    @Scheduled(
            fixedDelayString =
                    "${app.mail.retry-scheduler-delay:600000}")
    public void processNotificationRetries() {


        LocalDateTime startTime =
                LocalDateTime.now();


        LOGGER.info(
                "Notification retry scheduler started at {}",
                startTime);



        try {


            retryService
                    .processRetryNotifications();



            LocalDateTime endTime =
                    LocalDateTime.now();


            LOGGER.info(
                    "Notification retry scheduler completed at {}",
                    endTime);



        } catch (Exception ex) {


            LOGGER.error(
                    "Notification retry scheduler failed",
                    ex);
        }
    }
}