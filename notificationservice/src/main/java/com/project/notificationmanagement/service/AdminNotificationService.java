package com.project.notificationmanagement.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.dto.response.NotificationStatisticsResponse;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;


public interface AdminNotificationService {


    Page<NotificationResponse> getAllNotifications(
            Pageable pageable);



    Page<NotificationResponse> getNotificationsByUserId(
            Long userId,
            Pageable pageable);



    Page<NotificationResponse> getNotificationsByStatus(
            NotificationStatus status,
            Pageable pageable);



    Page<NotificationResponse> getNotificationsByType(
            NotificationType type,
            Pageable pageable);



    Page<NotificationResponse> getNotificationsByBookingReference(
            String bookingReference,
            Pageable pageable);



    NotificationResponse retryNotification(
            String notificationId);



    void deleteNotification(
            String notificationId);



    NotificationStatisticsResponse getStatistics();

}