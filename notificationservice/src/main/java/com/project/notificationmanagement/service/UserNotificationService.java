package com.project.notificationmanagement.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.notificationmanagement.dto.response.NotificationResponse;



public interface UserNotificationService {


    Page<NotificationResponse> getMyNotifications(
            Long userId,
            Pageable pageable);


    NotificationResponse getMyNotification(
            String notificationId,
            Long userId);


    Page<NotificationResponse> getMyBookingNotifications(
            String bookingReference,
            Long userId,
            Pageable pageable);


}