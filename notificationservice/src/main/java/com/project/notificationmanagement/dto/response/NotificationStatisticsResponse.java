package com.project.notificationmanagement.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationStatisticsResponse {

    private Long totalNotifications;

    private Long totalEmailNotifications;

    private Long totalSmsNotifications;

    private Long totalInAppNotifications;

    private Long totalPushNotifications;

    private Long totalBookingNotifications;

    private Long totalPaymentNotifications;

    private Long totalRefundNotifications;

    private Long totalVerificationNotifications;

    private Long totalPasswordResetNotifications;

    private Long pendingNotifications;

    private Long processingNotifications;

    private Long sentNotifications;

    private Long failedNotifications;

    private Long cancelledNotifications;

    private Long totalRetries;

    private Double successRate;

    private Double failureRate;

    private LocalDateTime generatedAt;
}