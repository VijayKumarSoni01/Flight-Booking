package com.project.notificationmanagement.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.notificationmanagement.enums.NotificationChannel;
import com.project.notificationmanagement.enums.NotificationStatus;
import com.project.notificationmanagement.enums.NotificationType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {


    @Id
    private String id;


    @NotNull(message = "Notification type is required")
    @Indexed
    private NotificationType notificationType;


    @NotNull(message = "Notification channel is required")
    @Indexed
    private NotificationChannel notificationChannel;


    @NotNull(message = "Notification status is required")
    @Indexed
    private NotificationStatus notificationStatus;


    @Indexed
    private Long userId;


    @NotBlank(message = "Recipient name is required")
    @Size(max = 100)
    private String recipientName;


    @Email(message = "Invalid email address")
    @NotBlank(message = "Recipient email is required")
    @Indexed
    private String recipientEmail;


    @Indexed
    @Size(max = 30)
    private String bookingReference;


    @Size(max = 20)
    private String pnr;


    @NotBlank(message = "Email subject is required")
    private String subject;


    private String body;


    private String templateName;


    private String providerMessageId;


    private String failureReason;


    @Builder.Default
    private Integer retryCount = 0;


    private LocalDateTime sentAt;


    private LocalDateTime lastRetryAt;


    private LocalDateTime nextRetryAt;


    @CreatedDate
    private LocalDateTime createdAt;


    @LastModifiedDate
    private LocalDateTime updatedAt;

}