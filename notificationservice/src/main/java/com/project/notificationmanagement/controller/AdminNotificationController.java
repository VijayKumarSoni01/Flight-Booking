package com.project.notificationmanagement.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.notificationmanagement.dto.common.ApiResponse;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.dto.response.NotificationStatisticsResponse;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;
import com.project.notificationmanagement.service.AdminNotificationService;
import com.project.notificationmanagement.service.RetryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/private/admin/notifications")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {


    private final AdminNotificationService adminNotificationService;

    private final RetryService retryService;



    @Operation(summary = "Get all notifications")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getAllNotifications(
            Pageable pageable) {


        Page<NotificationResponse> response =
                adminNotificationService
                .getAllNotifications(pageable);


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Notifications fetched successfully."
                )
        );
    }





    @Operation(summary = "Get notifications by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getByUser(
            @PathVariable Long userId,
            Pageable pageable) {


        Page<NotificationResponse> response =
                adminNotificationService
                .getNotificationsByUserId(
                        userId,
                        pageable
                );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "User notifications fetched successfully."
                )
        );
    }






    @Operation(summary = "Filter by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getByStatus(
            @PathVariable NotificationStatus status,
            Pageable pageable) {


        Page<NotificationResponse> response =
                adminNotificationService
                .getNotificationsByStatus(
                        status,
                        pageable
                );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Notifications fetched successfully."
                )
        );
    }







    @Operation(summary = "Filter by type")
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getByType(
            @PathVariable NotificationType type,
            Pageable pageable) {


        Page<NotificationResponse> response =
                adminNotificationService
                .getNotificationsByType(
                        type,
                        pageable
                );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Notifications fetched successfully."
                )
        );
    }







    @Operation(summary = "Retry failed notification")
    @PostMapping("/{notificationId}/retry")
    public ResponseEntity<ApiResponse<NotificationResponse>> retryNotification(
            @PathVariable String notificationId) {


        NotificationResponse response =
                retryService
                .retryNotification(notificationId);



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Notification retry completed successfully."
                )
        );
    }







    @Operation(summary = "Delete notification")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable String notificationId) {


        adminNotificationService
        .deleteNotification(notificationId);



        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Notification deleted successfully."
                )
        );
    }







    @Operation(summary = "Notification statistics")
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<NotificationStatisticsResponse>> statistics() {


        NotificationStatisticsResponse response =
                adminNotificationService
                .getStatistics();



        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Statistics fetched successfully."
                )
        );
    }

}