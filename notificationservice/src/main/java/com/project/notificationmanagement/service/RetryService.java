package com.project.notificationmanagement.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.notificationmanagement.dto.response.NotificationResponse;


public interface RetryService {


    NotificationResponse retryNotification(
            String notificationId);



    void processRetryNotifications();



    Page<NotificationResponse> getFailedNotifications(
            Pageable pageable);



    long getRetryCount(
            String notificationId);



    boolean canRetry(
            String notificationId);



    LocalDateTime getNextRetryTime(
            String notificationId);



    void cancelRetry(
            String notificationId);



    void resetRetryCount(
            String notificationId);

}