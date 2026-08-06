package com.project.payment.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;

public interface PaymentAdminService {

    Page<PaymentResDTO> getAllPayments(
            Pageable pageable);

    Page<PaymentResDTO> getPaymentsByStatus(
            PaymentStatus paymentStatus,
            Pageable pageable);

    Page<PaymentResDTO> getPaymentsByGateway(
            PaymentGateway paymentGateway,
            Pageable pageable);

    Page<PaymentResDTO> getPaymentsByUserId(
            Long userId,
            Pageable pageable);
}