package com.project.notificationmanagement.service.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.dto.response.NotificationStatisticsResponse;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;
import com.project.notificationmanagement.mapper.NotificationMapper;
import com.project.notificationmanagement.repository.NotificationRepository;
import com.project.notificationmanagement.service.AdminNotificationService;
import com.project.notificationmanagement.service.RetryService;


import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AdminNotificationServiceImpl 
        implements AdminNotificationService {



    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final RetryService retryService;





    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getAllNotifications(
            Pageable pageable) {


        return notificationRepository
                .findAll(pageable)
                .map(notificationMapper::toNotificationResponse);
    }






    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByUserId(
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
    public Page<NotificationResponse> getNotificationsByStatus(
            NotificationStatus status,
            Pageable pageable) {


        return notificationRepository
                .findByNotificationStatus(
                        status,
                        pageable)
                .map(notificationMapper::toNotificationResponse);
    }






    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByType(
            NotificationType type,
            Pageable pageable) {


        return notificationRepository
                .findByNotificationType(
                        type,
                        pageable)
                .map(notificationMapper::toNotificationResponse);
    }






    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByBookingReference(
            String bookingReference,
            Pageable pageable) {


        return notificationRepository
                .findByBookingReference(
                        bookingReference,
                        pageable)
                .map(notificationMapper::toNotificationResponse);
    }







    @Override
    @Transactional
    public NotificationResponse retryNotification(
            String notificationId) {


        return retryService
                .retryNotification(notificationId);
    }







    @Override
    @Transactional
    public void deleteNotification(
            String notificationId) {


        notificationRepository
                .deleteById(notificationId);
    }







    @Override
    @Transactional(readOnly = true)
    public NotificationStatisticsResponse getStatistics() {


        long total =
                notificationRepository.count();



        long sent =
                notificationRepository
                .countByNotificationStatus(
                        NotificationStatus.SENT);



        long failed =
                notificationRepository
                .countByNotificationStatus(
                        NotificationStatus.FAILED);



        double successRate =
                total == 0
                ? 0
                : ((double) sent / total) * 100;



        double failureRate =
                total == 0
                ? 0
                : ((double) failed / total) * 100;



        return NotificationStatisticsResponse
                .builder()

                .totalNotifications(total)

                .sentNotifications(sent)

                .failedNotifications(failed)

                .successRate(successRate)

                .failureRate(failureRate)

                .build();
    }

}