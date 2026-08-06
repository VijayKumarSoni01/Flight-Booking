package com.project.payment.dto.common;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {

    private String message;
    private LocalDateTime timestamp;
}
