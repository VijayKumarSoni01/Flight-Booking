package com.project.payment.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.project.payment.client.BookingServiceClient;
import com.project.payment.config.payment.RazorpayProperties;
import com.project.payment.config.payment.StripeProperties;
import com.project.payment.dto.request.UpdateBookingPaymentStatusReqDTO;
import com.project.payment.entity.Payment;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.exception.PaymentProcessingException;
import com.project.payment.repository.PaymentRepository;
import com.project.payment.service.interfaces.WebhookService;
import com.razorpay.Utils;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WebhookServiceImpl implements WebhookService {

    private final PaymentRepository paymentRepository;

    private final BookingServiceClient bookingServiceClient;

    private final RazorpayProperties razorpayProperties;

    private final StripeProperties stripeProperties;

    @Override
    public void processRazorpayWebhook(
            String payload,
            String signature) {

        try {

            boolean verified = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    razorpayProperties.getWebhookSecret());

            if (!verified) {
                throw new PaymentProcessingException(
                        "Invalid Razorpay webhook signature.");
            }

            JSONObject webhook = new JSONObject(payload);

            String event = webhook.getString("event");

            if (!"payment.captured".equals(event)) {
                return;
            }

            JSONObject paymentEntity = webhook
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String gatewayPaymentId = paymentEntity.getString("id");

            String gatewayOrderId = paymentEntity.getString("order_id");

            String status = paymentEntity.getString("status");

            Payment payment = paymentRepository
                    .findByGatewayPaymentId(gatewayPaymentId)
                    .orElseThrow(() -> new PaymentProcessingException(
                            "Payment not found for gateway payment id: "
                                    + gatewayPaymentId));

            if (Boolean.TRUE.equals(payment.getWebhookVerified())) {
                return;
            }

            if (!"captured".equals(status)) {
                return;
            }

            payment.setPaymentStatus(PaymentStatus.SUCCESS);

            if (payment.getPaidAt() == null) {
                payment.setPaidAt(LocalDateTime.now());
            }

            if (payment.getGatewayOrderId() == null) {
                payment.setGatewayOrderId(gatewayOrderId);
            }

            if (payment.getGatewayPaymentId() == null) {
                payment.setGatewayPaymentId(gatewayPaymentId);
            }

            payment.setGatewayResponse(payload);

            payment.setWebhookVerified(true);

            if (payment.getTransactionId() == null) {
                payment.setTransactionId(
                        payment.getPaymentGateway().name()
                                + "-"
                                + gatewayPaymentId);
            }

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

        } catch (Exception ex) {

            throw new PaymentProcessingException(
                    "Unable to process Razorpay webhook.",
                    ex);
        }
    }

    @Override
    public void processStripeWebhook(
            String payload,
            String signature) {

        try {

            Event event = Webhook.constructEvent(
                    payload,
                    signature,
                    stripeProperties.getWebhookSecret());

            if (!"payment_intent.succeeded".equals(event.getType())) {
                return;
            }

            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new PaymentProcessingException(
                            "Unable to deserialize PaymentIntent."));
            String gatewayOrderId = paymentIntent.getId();

            String gatewayPaymentId = paymentIntent.getLatestCharge();

            Payment payment = paymentRepository
                    .findByGatewayOrderId(gatewayOrderId)
                    .orElseThrow(() -> new PaymentProcessingException(
                            "Payment not found."));

            if (Boolean.TRUE.equals(payment.getWebhookVerified())) {
                return;
            }

            payment.setGatewayPaymentId(
                    gatewayPaymentId);

            payment.setPaymentStatus(
                    PaymentStatus.SUCCESS);

            payment.setGatewayResponse(
                    payload);

            payment.setWebhookVerified(true);

            if (payment.getTransactionId() == null) {

                payment.setTransactionId(
                        payment.getPaymentGateway().name()
                                + "-"
                                + gatewayPaymentId);
            }

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

        } catch (SignatureVerificationException ex) {

            throw new PaymentProcessingException(
                    "Invalid Stripe webhook signature.",
                    ex);

        } catch (Exception ex) {

            throw new PaymentProcessingException(
                    "Unable to process Stripe webhook.",
                    ex);
        }
    }

}
