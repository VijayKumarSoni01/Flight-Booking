package com.project.notificationmanagement.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.project.notificationmanagement.config.security.SecurityUtils;
import com.project.notificationmanagement.dto.common.ApiResponse;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.service.UserNotificationService;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/private/notifications")
@RequiredArgsConstructor
public class UserNotificationController {


    private final UserNotificationService userNotificationService;

    private final SecurityUtils securityUtils;




    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getMyNotifications(
            Pageable pageable) {


        Long userId =
                securityUtils.getCurrentUserId();



        Page<NotificationResponse> response =
                userNotificationService
                        .getMyNotifications(
                                userId,
                                pageable
                        );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Your notifications fetched successfully."
                )
        );
    }






    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getMyNotification(
            @PathVariable String notificationId) {


        Long userId =
                securityUtils.getCurrentUserId();



        NotificationResponse response =
                userNotificationService
                        .getMyNotification(
                                notificationId,
                                userId
                        );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Notification fetched successfully."
                )
        );
    }







    @GetMapping("/booking/{bookingReference}")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getBookingNotifications(
            @PathVariable String bookingReference,
            Pageable pageable) {


        Long userId =
                securityUtils.getCurrentUserId();



        Page<NotificationResponse> response =
                userNotificationService
                        .getMyBookingNotifications(
                                bookingReference,
                                userId,
                                pageable
                        );


        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking notifications fetched successfully."
                )
        );
    }

}