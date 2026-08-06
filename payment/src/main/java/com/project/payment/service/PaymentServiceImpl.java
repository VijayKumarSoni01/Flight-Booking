package com.project.payment.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.project.payment.client.BookingServiceClient;
import com.project.payment.config.payment.RazorpayProperties;
import com.project.payment.dto.request.CreatePaymentReqDTO;
import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.RetryPaymentReqDTO;
import com.project.payment.dto.request.UpdateBookingPaymentStatusReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.BookingValidationResDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.dto.response.PaymentStatusResDTO;
import com.project.payment.dto.response.PaymentSummaryResDTO;
import com.project.payment.dto.response.RefundResponseDTO;
import com.project.payment.entity.Payment;
import com.project.payment.enums.CurrencyCode;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.exception.PaymentNotFoundException;
import com.project.payment.exception.PaymentProcessingException;
import com.project.payment.exception.PaymentRefundException;
import com.project.payment.mapper.PaymentMapper;
import com.project.payment.repository.PaymentRepository;
import com.project.payment.service.gateway.GatewayFactory;
import com.project.payment.service.gateway.PaymentGatewayService;
import com.project.payment.service.interfaces.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

        private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

        private final PaymentRepository paymentRepository;
        private final GatewayFactory gatewayFactory;
        private final PaymentMapper paymentMapper;
        private final BookingServiceClient bookingServiceClient;
        private final RazorpayProperties razorpayProperties;

        @Override
        @Transactional
        public PaymentConfirmationResDTO createPayment(
                        CreatePaymentReqDTO request) {

                log.info("Razorpay Key ID: {}", razorpayProperties.getKeyId());
                log.info("Razorpay Key Secret: {}", razorpayProperties.getKeySecret());

                log.info(
                                "Creating payment for Booking Reference={}",
                                request.getBookingReference());

                // Validate Booking
                BookingValidationResDTO booking = bookingServiceClient.getBookingByReference(
                                request.getBookingReference());

                if (booking == null) {

                        throw new PaymentProcessingException(
                                        "Booking not found.");
                }

                // Booking already paid
                if ("SUCCESS".equalsIgnoreCase(booking.getPaymentStatus())) {

                        throw new PaymentProcessingException(
                                        "Booking has already been paid.");
                }

                if (!"PENDING".equalsIgnoreCase(
                                booking.getBookingStatus())) {

                        throw new PaymentProcessingException(
                                        "Payment can only be created for pending bookings.");
                }

                // Prevent duplicate successful payments
                if (paymentRepository.existsByBookingIdAndPaymentStatus(
                                booking.getBookingId(),
                                PaymentStatus.SUCCESS)) {

                        throw new PaymentProcessingException(
                                        "Payment has already been completed for this booking.");
                }

                // Create Payment Entity
                Payment payment = paymentMapper.toEntity(request);

                payment.setBookingId(
                                booking.getBookingId());

                payment.setUserId(
                                booking.getUserId());

                payment.setAmount(
                                booking.getTotalAmount());

                try {

                        payment.setCurrency(
                                        CurrencyCode.valueOf(
                                                        booking.getCurrency()));

                } catch (IllegalArgumentException ex) {

                        throw new PaymentProcessingException(
                                        "Unsupported currency: "
                                                        + booking.getCurrency());
                }

                payment.setPaymentStatus(
                                PaymentStatus.PENDING);

                long previousAttempts = paymentRepository.countByBookingId(
                                booking.getBookingId());

                payment.setAttemptNumber(
                                (int) previousAttempts + 1);

                payment.setRetryCount(
                                (int) previousAttempts);

                payment.setWebhookVerified(false);

                payment.setIdempotencyKey(
                                UUID.randomUUID().toString());

                payment = paymentRepository.save(payment);

                log.info(
                                "Payment record created successfully. PaymentId={}",
                                payment.getId());

                try {

                        PaymentGatewayService gateway = gatewayFactory.getGateway(
                                        payment.getPaymentGateway());

                        PaymentConfirmationResDTO response = gateway.createPayment(payment);

                        paymentRepository.save(payment);

                        log.info(
                                        "Gateway order created successfully. GatewayOrderId={}",
                                        response.getGatewayOrderId());

                        return response;

                } catch (Exception ex) {

                        payment.setPaymentStatus(
                                        PaymentStatus.FAILED);

                        payment.setFailureReason(
                                        ex.getMessage());

                        paymentRepository.save(payment);

                        log.error(
                                        "Payment creation failed for Booking Reference={}",
                                        request.getBookingReference(),
                                        ex);

                        throw ex;
                }
        }

        @Override
        public PaymentStatusResDTO verifyPayment(
                        VerifyPaymentReqDTO request) {

                log.info("Verifying payment {}",
                                request.getGatewayOrderId());

                Payment payment = paymentRepository
                                .findByGatewayOrderId(
                                                request.getGatewayOrderId())
                                .orElseThrow(() -> new PaymentNotFoundException(
                                                "Payment not found."));

                if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {

                        throw new PaymentProcessingException(
                                        "Payment already verified.");
                }

                try {

                        PaymentGatewayService gateway = gatewayFactory.getGateway(
                                        payment.getPaymentGateway());

                        log.info(
                                        "Calling {} gateway verification.",
                                        payment.getPaymentGateway());

                        gateway.verifyPayment(
                                        payment,
                                        request);

                        payment = paymentRepository.save(payment);

                        UpdateBookingPaymentStatusReqDTO bookingRequest = UpdateBookingPaymentStatusReqDTO.builder()
                                        .paymentStatus(payment.getPaymentStatus().name())
                                        .transactionId(payment.getTransactionId())
                                        .gatewayOrderId(payment.getGatewayOrderId())
                                        .gatewayPaymentId(payment.getGatewayPaymentId())
                                        .build();

                        bookingServiceClient.updatePaymentStatus(
                                        payment.getBookingId(),
                                        bookingRequest);

                        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {

                                bookingServiceClient.confirmBooking(
                                                payment.getBookingId());

                                log.info("Booking confirmed successfully. BookingId={}",
                                                payment.getBookingId());
                        }

                        return paymentMapper.toPaymentStatusResDTO(payment);

                } catch (Exception ex) {

                        payment.setPaymentStatus(PaymentStatus.FAILED);
                        payment.setFailureReason(ex.getMessage());

                        paymentRepository.save(payment);

                        log.error("Payment verification failed", ex);

                        throw ex;
                }
        }

        @Override
        @Transactional
        public RefundResponseDTO refundPayment(
                        RefundPaymentReqDTO request) {

                log.info(
                                "Refund requested for BookingId={}",
                                request.getBookingId());

                Payment payment = paymentRepository
                                .findTopByBookingIdOrderByAttemptNumberDesc(
                                                request.getBookingId())
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Payment not found for BookingId={}",
                                                        request.getBookingId());

                                        return new PaymentNotFoundException(
                                                        "Payment not found for booking id: "
                                                                        + request.getBookingId());
                                });

                if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {

                        log.warn(
                                        "Refund rejected. Payment is not successful. BookingId={}",
                                        request.getBookingId());

                        throw new PaymentRefundException(
                                        "Only successful payments can be refunded.");
                }

                if (payment.getRefundAmount() != null) {

                        log.warn(
                                        "Refund rejected. Payment already refunded. BookingId={}",
                                        request.getBookingId());

                        throw new PaymentRefundException(
                                        "Payment has already been refunded.");
                }

                if (request.getRefundAmount().compareTo(payment.getAmount()) > 0) {

                        log.warn(
                                        "Refund amount exceeds payment amount. BookingId={}",
                                        request.getBookingId());

                        throw new PaymentRefundException(
                                        "Refund amount cannot exceed payment amount.");
                }

                PaymentGatewayService gateway = gatewayFactory.getGateway(
                                payment.getPaymentGateway());

                gateway.refundPayment(
                                payment,
                                request);

                payment = paymentRepository.save(payment);

                log.info(
                                "Refund processed successfully. PaymentId={}, RefundId={}",
                                payment.getId(),
                                payment.getGatewayRefundId());

                RefundResponseDTO response = paymentMapper.toRefundResponseDTO(payment);

                response.setMessage(
                                "Refund processed successfully.");

                return response;
        }

        @Override
        public PaymentConfirmationResDTO retryPayment(
                        RetryPaymentReqDTO request) {

                log.info(
                                "Retry requested for PaymentId={}",
                                request.getPaymentId());

                Payment previousPayment = paymentRepository.findById(request.getPaymentId())
                                .orElseThrow(() -> new PaymentNotFoundException(
                                                "Payment not found."));

                if (previousPayment.getPaymentStatus() == PaymentStatus.SUCCESS) {

                        throw new PaymentProcessingException(
                                        "Successful payments cannot be retried.");
                }

                if (previousPayment.getRetryCount() >= 3) {

                        throw new PaymentProcessingException(
                                        "Maximum retry limit reached.");
                }

                Payment payment = Payment.builder()
                                .bookingId(previousPayment.getBookingId())
                                .bookingReference(previousPayment.getBookingReference())
                                .userId(previousPayment.getUserId())
                                .amount(previousPayment.getAmount())
                                .currency(previousPayment.getCurrency())
                                .paymentMethod(request.getPaymentMethod())
                                .paymentGateway(request.getPaymentGateway())
                                .description(previousPayment.getDescription())
                                .paymentStatus(PaymentStatus.PENDING)
                                .attemptNumber(previousPayment.getAttemptNumber() + 1)
                                .retryCount(previousPayment.getRetryCount() + 1)
                                .webhookVerified(false)
                                .idempotencyKey(UUID.randomUUID().toString())
                                .build();

                payment = paymentRepository.save(payment);

                PaymentGatewayService gateway = gatewayFactory.getGateway(
                                payment.getPaymentGateway());

                PaymentConfirmationResDTO response = gateway.createPayment(payment);

                paymentRepository.save(payment);

                log.info(
                                "Retry payment created successfully. PaymentId={}",
                                payment.getId());

                return response;
        }

        @Override
        public PaymentResDTO getPaymentById(Long paymentId) {

                Payment payment = paymentRepository.findById(paymentId)
                                .orElseThrow(() -> new PaymentNotFoundException(
                                                "Payment not found with id: " + paymentId));

                log.info("Fetching payment {}",
                                paymentId);

                return paymentMapper.toPaymentResDTO(payment);
        }

        @Override
        public List<PaymentSummaryResDTO> getPaymentsByBookingReference(
                        String bookingReference) {

                List<Payment> payments = paymentRepository.findByBookingIdOrderByAttemptNumberAsc(
                                paymentRepository
                                                .findTopByBookingReferenceOrderByAttemptNumberDesc(
                                                                bookingReference)
                                                .orElseThrow(() -> new PaymentNotFoundException(
                                                                "Payment not found."))
                                                .getBookingId());

                return paymentMapper.toPaymentSummaryResDTOList(payments);
        }

        @Override
        public PaymentStatusResDTO getPaymentStatus(
                        String bookingReference) {

                Payment payment = paymentRepository
                                .findTopByBookingReferenceOrderByAttemptNumberDesc(
                                                bookingReference)
                                .orElseThrow(() -> new PaymentNotFoundException(
                                                "Payment not found."));

                log.info(
                                "Fetching payment status for Booking Reference={}",
                                bookingReference);

                return paymentMapper.toPaymentStatusResDTO(payment);
        }
}