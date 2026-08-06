package com.project.payment.service.gateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.project.payment.config.payment.RazorpayProperties;
import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.RefundResponseDTO;
import com.project.payment.entity.Payment;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.exception.PaymentProcessingException;
import com.project.payment.exception.PaymentRefundException;
import com.project.payment.exception.PaymentVerificationException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorpayGateway implements PaymentGatewayService {

        private final RazorpayClient razorpayClient;
        private final RazorpayProperties razorpayProperties;

        @Override
        public PaymentGateway getGateway() {
                return PaymentGateway.RAZORPAY;
        }

        @Override
        public PaymentConfirmationResDTO createPayment(Payment payment) {

                try {

                        JSONObject request = new JSONObject();

                        request.put(
                                        "amount",
                                        convertToSmallestUnit(payment.getAmount()));

                        request.put(
                                        "currency",
                                        payment.getCurrency().name());

                        request.put(
                                        "receipt",
                                        payment.getBookingReference());

                        request.put(
                                        "payment_capture",
                                        1);

                        log.info(
                                        "Creating Razorpay Order for Booking Reference: {}",
                                        payment.getBookingReference());

                        log.info(
                                        "Request JSON = {}",
                                        request);

                        // Create Razorpay Order
                        Order order = razorpayClient.orders.create(request);

                        log.info("Order Created Successfully.");

                        // Read Order Id only once
                        String orderId = order.get("id").toString();

                        // Save Order Id in Payment Entity
                        payment.setGatewayOrderId(orderId);

                        log.info(
                                        "Razorpay Order Created Successfully. OrderId={}",
                                        orderId);

                        // Return Response
                        return PaymentConfirmationResDTO.builder()
                                        .paymentId(payment.getId())
                                        .bookingReference(payment.getBookingReference())
                                        .gatewayOrderId(orderId)
                                        .paymentStatus(PaymentStatus.PENDING)
                                        .amount(payment.getAmount())
                                        .currency(payment.getCurrency())
                                        .paymentGateway(PaymentGateway.RAZORPAY)

                                        // Public Razorpay Key (Frontend Checkout)
                                        .keyId(razorpayProperties.getKeyId())

                                        // Not required for Razorpay Checkout
                                        .paymentUrl(null)
                                        .expiresAt(null)
                                        .clientSecret(null)

                                        .build();

                } catch (RazorpayException ex) {

                        log.error(
                                        "Unable to create Razorpay order for booking {}",
                                        payment.getBookingReference(),
                                        ex);

                        throw new PaymentProcessingException(
                                        "Unable to create Razorpay order.",
                                        ex);

                } catch (Exception ex) {

                        log.error(
                                        "Unexpected error while creating Razorpay order.",
                                        ex);

                        throw new PaymentProcessingException(
                                        "Unexpected error while creating Razorpay order.",
                                        ex);
                }
        }

        @Override
        public void verifyPayment(
                        Payment payment,
                        VerifyPaymentReqDTO request) {

                try {

                        JSONObject attributes = new JSONObject();

                        attributes.put(
                                        "razorpay_order_id",
                                        request.getGatewayOrderId());

                        attributes.put(
                                        "razorpay_payment_id",
                                        request.getGatewayPaymentId());

                        attributes.put(
                                        "razorpay_signature",
                                        request.getSignature());

                        boolean verified = com.razorpay.Utils.verifyPaymentSignature(
                                        attributes,
                                        razorpayProperties.getKeySecret());

                        if (!verified) {

                                throw new PaymentVerificationException(
                                                "Invalid Razorpay payment signature.");
                        }

                        payment.setGatewayOrderId(
                                        request.getGatewayOrderId());

                        payment.setGatewayPaymentId(
                                        request.getGatewayPaymentId());

                        payment.setTransactionId(
                                        PaymentGateway.RAZORPAY.name()
                                                        + "-"
                                                        + request.getGatewayPaymentId());

                        payment.setPaymentStatus(
                                        PaymentStatus.SUCCESS);

                        payment.setPaidAt(
                                        LocalDateTime.now());

                        payment.setGatewayResponse(
                                        request.getGatewayResponse());

                        log.info(
                                        "Payment verified successfully. PaymentId=" + request.getGatewayPaymentId());

                } catch (PaymentVerificationException ex) {

                        throw ex;

                } catch (Exception ex) {

                        log.error(
                                        "Unable to verify Razorpay payment.",
                                        ex);

                        throw new PaymentVerificationException(
                                        "Unable to verify Razorpay payment.",
                                        ex);
                }
        }

        @Override
        public RefundResponseDTO refundPayment(
                        Payment payment,
                        RefundPaymentReqDTO request) {

                try {

                        JSONObject refundRequest = new JSONObject();

                        refundRequest.put(
                                        "payment_id",
                                        payment.getGatewayPaymentId());

                        refundRequest.put(
                                        "amount",
                                        convertToSmallestUnit(
                                                        request.getRefundAmount()));

                        refundRequest.put(
                                        "notes",
                                        new JSONObject()
                                                        .put(
                                                                        "reason",
                                                                        request.getReason()));

                        Refund refund = razorpayClient.refunds.create(
                                        refundRequest);

                        payment.setGatewayRefundId(
                                        refund.get("id").toString());

                        payment.setRefundAmount(
                                        request.getRefundAmount());

                        payment.setRefundedAt(
                                        LocalDateTime.now());

                        payment.setPaymentStatus(
                                        PaymentStatus.REFUNDED);

                        log.info(
                                        "Refund processed successfully. RefundId=" + refund.get("id"));

                        return RefundResponseDTO.builder()
                                        .paymentId(payment.getId())
                                        .refundAmount(payment.getRefundAmount())
                                        .paymentStatus(payment.getPaymentStatus())
                                        .refundedAt(payment.getRefundedAt())
                                        .message("Refund processed successfully.")
                                        .build();

                } catch (RazorpayException ex) {

                        log.error(
                                        "Unable to process Razorpay refund.",
                                        ex);

                        throw new PaymentRefundException(
                                        "Unable to process Razorpay refund.",
                                        ex);
                }
        }

        private long convertToSmallestUnit(
                        BigDecimal amount) {

                return amount.multiply(
                                BigDecimal.valueOf(100))
                                .longValue();
        }
}