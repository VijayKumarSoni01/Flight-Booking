package com.project.payment.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatisticsResDTO {
    
    private long totalPayments;

    private long successfulPayments;

    private long failedPayments;

    private long refundedPayments;

    private BigDecimal totalRevenue;

    private BigDecimal totalRefunds;
}
