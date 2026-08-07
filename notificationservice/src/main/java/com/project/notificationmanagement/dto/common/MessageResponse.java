package com.project.notificationmanagement.dto.common;

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
public class MessageResponse {

    private boolean success;

    private int status;

    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String path;
}