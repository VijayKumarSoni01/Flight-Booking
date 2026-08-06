package com.project.bookingmanagement.dto.booking.request;



import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingPaymentStatusReqDTO {

    private PaymentStatus paymentStatus;
}
