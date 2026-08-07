package com.project.notificationmanagement.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.entity.Notification;
import com.project.notificationmanagement.exception.NotificationNotFoundException;
import com.project.notificationmanagement.mapper.NotificationMapper;
import com.project.notificationmanagement.repository.NotificationRepository;
import com.project.notificationmanagement.service.UserNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl
        implements UserNotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(
            Long userId,
            Pageable pageable) {

        return notificationRepository
                .findByUserId(
                        userId,
                        pageable)
                .map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getMyNotification(
            String notificationId,
            Long userId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(
                        () -> new NotificationNotFoundException(
                                "Notification not found with id : "
                                        + notificationId));

        if (!notification.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You cannot access this notification");
        }

        return notificationMapper
                .toNotificationResponse(
                        notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyBookingNotifications(
            String bookingReference,
            Long userId,
            Pageable pageable) {

        return notificationRepository
                .findByBookingReferenceAndUserId(
                        bookingReference,
                        userId,
                        pageable)
                .map(notificationMapper::toNotificationResponse);
    }

}