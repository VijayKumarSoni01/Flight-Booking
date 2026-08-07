package com.project.notificationmanagement.dto.common;

import java.time.LocalDateTime;
import java.util.Map;

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
public class ErrorResponse {

    private boolean success;

    private int status;

    private String error;

    private String errorCode;

    private String message;

    private String path;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Map<String, String> validationErrors;
}