package com.project.payment.service;

// import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.mapper.PaymentMapper;
import com.project.payment.repository.PaymentRepository;
import com.project.payment.service.interfaces.PaymentAdminService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentAdminServiceImpl implements PaymentAdminService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public Page<PaymentResDTO> getAllPayments(
            Pageable pageable) {

        return paymentRepository
                .findAll(pageable)
                .map(paymentMapper::toPaymentResDTO);
    }

    @Override
    @Transactional
    public Page<PaymentResDTO> getPaymentsByStatus(
            PaymentStatus paymentStatus,
            Pageable pageable) {

        return paymentRepository
                .findByPaymentStatus(
                        paymentStatus,
                        pageable)
                .map(paymentMapper::toPaymentResDTO);
    }

    @Override
    @Transactional
    public Page<PaymentResDTO> getPaymentsByGateway(
            PaymentGateway paymentGateway,
            Pageable pageable) {

        return paymentRepository
                .findByPaymentGateway(
                        paymentGateway,
                        pageable)
                .map(paymentMapper::toPaymentResDTO);
    }

    @Override
    @Transactional
    public Page<PaymentResDTO> getPaymentsByUserId(
            Long userId,
            Pageable pageable) {

        return paymentRepository
                .findByUserId(
                        userId,
                        pageable)
                .map(paymentMapper::toPaymentResDTO);
    }

}