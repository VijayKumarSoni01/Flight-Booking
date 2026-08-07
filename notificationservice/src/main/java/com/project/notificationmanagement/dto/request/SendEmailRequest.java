package com.project.notificationmanagement.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class SendEmailRequest {

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String recipientEmail;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Email body is required")
    private String body;

    @Builder.Default
    private boolean html = true;

    private List<@Email(message = "Invalid CC email") String> cc;

    private List<@Email(message = "Invalid BCC email") String> bcc;

    private List<String> attachments;

    private String fromName;

    @Email(message = "Invalid reply-to email")
    private String replyTo;
}