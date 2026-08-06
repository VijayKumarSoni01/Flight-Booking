package com.project.payment.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebhookAckResponse {

    private boolean received;
    private String message;
}
