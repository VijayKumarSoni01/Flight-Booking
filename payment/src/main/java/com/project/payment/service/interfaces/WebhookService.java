package com.project.payment.service.interfaces;

public interface WebhookService {

    void processRazorpayWebhook(
            String payload,
            String signature);

    void processStripeWebhook(
            String payload,
            String signature);

}