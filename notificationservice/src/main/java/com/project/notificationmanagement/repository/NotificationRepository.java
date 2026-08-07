package com.project.notificationmanagement.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.notificationmanagement.entity.Notification;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;



public interface NotificationRepository 
        extends MongoRepository<Notification, String> {



    /*
     * ==========================
     * USER NOTIFICATION HISTORY
     * ==========================
     */


    Page<Notification> findByUserId(
            Long userId,
            Pageable pageable);



    Page<Notification> findByUserIdAndNotificationStatus(
            Long userId,
            NotificationStatus notificationStatus,
            Pageable pageable);



    Page<Notification> findByUserIdAndNotificationType(
            Long userId,
            NotificationType notificationType,
            Pageable pageable);



    Page<Notification> findByBookingReferenceAndUserId(
            String bookingReference,
            Long userId,
            Pageable pageable);



    Optional<Notification> findByIdAndUserId(
            String id,
            Long userId);




    /*
     * ==========================
     * ADMIN MANAGEMENT
     * ==========================
     */



    Page<Notification> findByNotificationStatus(
            NotificationStatus notificationStatus,
            Pageable pageable);



    Page<Notification> findByNotificationType(
            NotificationType notificationType,
            Pageable pageable);



    Page<Notification> findByBookingReference(
            String bookingReference,
            Pageable pageable);



    Page<Notification> findByRecipientEmail(
            String recipientEmail,
            Pageable pageable);




    /*
     * ==========================
     * RETRY SYSTEM
     * ==========================
     */



    List<Notification> findByNotificationStatusAndNextRetryAtBefore(
            NotificationStatus notificationStatus,
            LocalDateTime time);




    /*
     * ==========================
     * STATISTICS
     * ==========================
     */



    long countByNotificationStatus(
            NotificationStatus notificationStatus);



    long countByNotificationType(
            NotificationType notificationType);



    long countByUserId(
            Long userId);



    long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end);




    /*
     * ==========================
     * DUPLICATE CHECK
     * ==========================
     */



    boolean existsByProviderMessageId(
            String providerMessageId);




    /*
     * ==========================
     * CLEANUP
     * ==========================
     */



    void deleteByCreatedAtBefore(
            LocalDateTime cutoffDate);

}