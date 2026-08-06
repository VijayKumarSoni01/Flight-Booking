package com.project.payment.service.gateway;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.payment.enums.PaymentGateway;

@Component
public class GatewayFactory {

    private final Map<PaymentGateway, PaymentGatewayService> gateways;

    public GatewayFactory(List<PaymentGatewayService> gatewayServices) {
        this.gateways = gatewayServices.stream()
                .collect(Collectors.toMap(
                        gateway -> gateway.getGateway(),
                        gateway -> gateway));
    }

    public PaymentGatewayService getGateway(PaymentGateway paymentGateway) {

        PaymentGatewayService gateway = gateways.get(paymentGateway);

        if (gateway == null) {
            throw new IllegalArgumentException(
                    "Unsupported payment gateway: " + paymentGateway);
        }

        return gateway;
    }
}