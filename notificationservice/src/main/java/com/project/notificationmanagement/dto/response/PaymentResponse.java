package com.project.notificationmanagement.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.CurrencyCode;
import com.project.notificationmanagement.enums.PaymentGateway;
import com.project.notificationmanagement.enums.PaymentMethod;
import com.project.notificationmanagement.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentResponse {


    private Long paymentId;


    private Long userId;


    private String bookingReference;


    private String transactionId;


    private BigDecimal amount;


    private CurrencyCode currency;


    private PaymentMethod paymentMethod;


    private PaymentGateway paymentGateway;


    private PaymentStatus paymentStatus;


    private LocalDateTime paidAt;


    private LocalDateTime createdAt;

}