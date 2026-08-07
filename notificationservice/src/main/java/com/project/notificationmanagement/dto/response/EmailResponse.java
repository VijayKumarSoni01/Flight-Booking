package com.project.notificationmanagement.dto.response;

import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.NotificationStatus;

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
public class EmailResponse {

    private Long notificationId;

    private String recipientName;

    private String recipientEmail;

    private String subject;

    private NotificationStatus notificationStatus;

    private String providerMessageId;

    private LocalDateTime sentAt;

    private String message;

    private String failureReason;
}