package com.project.payment.service.gateway;

import com.project.payment.config.payment.StripeProperties;
import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.RefundResponseDTO;
import com.project.payment.entity.Payment;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;
import com.project.payment.exception.PaymentProcessingException;
// import com.project.payment.exception.PaymentRefundException;
import com.project.payment.exception.PaymentVerificationException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.model.Refund;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeGateway implements PaymentGatewayService {

    private final StripeProperties stripeProperties;

    @Override
    public PaymentGateway getGateway() {
        return PaymentGateway.STRIPE;
    }

    @Override
    public PaymentConfirmationResDTO createPayment(Payment payment) {

        try {

            Stripe.apiKey = stripeProperties.getSecretKey();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()

                    .setAmount(
                            convertToSmallestUnit(
                                    payment.getAmount()))

                    .setCurrency(
                            payment.getCurrency()
                                    .name()
                                    .toLowerCase())

                    .setDescription(
                            payment.getDescription())

                    .putMetadata(
                            "bookingReference",
                            payment.getBookingReference())

                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            payment.setGatewayOrderId(
                    paymentIntent.getId());

            return PaymentConfirmationResDTO.builder()
                    .paymentId(payment.getId())
                    .bookingReference(payment.getBookingReference())
                    .gatewayOrderId(paymentIntent.getId())
                    .paymentStatus(PaymentStatus.PENDING)
                    .amount(payment.getAmount())
                    .currency(payment.getCurrency())
                    .paymentGateway(PaymentGateway.STRIPE)
                    .paymentUrl(null)
                    .clientSecret(paymentIntent.getClientSecret())
                    .build();

        } catch (StripeException ex) {

            throw new PaymentProcessingException(
                    "Unable to create Stripe payment.",
                    ex);
        }
    }

    @Override
    public void verifyPayment(
            Payment payment,
            VerifyPaymentReqDTO request) {

        try {

            Stripe.apiKey = stripeProperties.getSecretKey();

            PaymentIntent paymentIntent = PaymentIntent.retrieve(
                    request.getGatewayPaymentId());

            if (!"succeeded".equals(paymentIntent.getStatus())) {

                throw new PaymentVerificationException(
                        "Stripe payment verification failed.");
            }

            payment.setGatewayOrderId(
                    paymentIntent.getId());

            payment.setGatewayPaymentId(
                    paymentIntent.getLatestCharge());

            payment.setTransactionId(
                    payment.getPaymentGateway().name()
                            + "-"
                            + payment.getGatewayPaymentId());

            payment.setGatewayResponse(
                    paymentIntent.toJson());

            payment.setPaymentStatus(
                    PaymentStatus.SUCCESS);

        } catch (PaymentVerificationException ex) {

            throw ex;

        } catch (StripeException ex) {

            throw new PaymentVerificationException(
                    "Unable to verify Stripe payment.",
                    ex);
        }
    }

    @Override
    public RefundResponseDTO refundPayment(
            Payment payment,
            RefundPaymentReqDTO request) {

        try {

            Stripe.apiKey = stripeProperties.getSecretKey();

            RefundCreateParams params = RefundCreateParams.builder()

                    .setCharge(
                            payment.getGatewayPaymentId())

                    .setAmount(
                            convertToSmallestUnit(
                                    request.getRefundAmount()))

                    .build();

            Refund refund = Refund.create(params);

            payment.setGatewayRefundId(
                    refund.getId());

            payment.setRefundAmount(
                    request.getRefundAmount());

            payment.setPaymentStatus(
                    PaymentStatus.REFUNDED);

            payment.setGatewayResponse(
                    refund.toJson());

            return RefundResponseDTO.builder()
                    .paymentId(payment.getId())
                    .refundAmount(payment.getRefundAmount())
                    .paymentStatus(payment.getPaymentStatus())
                    .refundedAt(payment.getRefundedAt())
                    .message("Refund processed successfully.")
                    .build();
            }

        // } catch (StripeException ex) {

        //     throw new PaymentRefundException(
        //             "Unable to process Stripe refund.",
        //             ex);
        // }
        catch (StripeException ex) {

    ex.printStackTrace();

    System.out.println("Stripe Error Type : " + ex.getClass().getSimpleName());
    System.out.println("Stripe Message    : " + ex.getMessage());

    throw new PaymentProcessingException(
            "Unable to create Stripe payment.",
            ex);
}
    }

    private long convertToSmallestUnit(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
}