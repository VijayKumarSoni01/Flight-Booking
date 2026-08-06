package com.project.payment.dto.request;

import com.project.payment.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingPaymentStatusReqDTO {

    private String paymentStatus;

    private BookingStatus bookingStatus;

    private String transactionId;

    private String gatewayOrderId;

    private String gatewayPaymentId;

}
