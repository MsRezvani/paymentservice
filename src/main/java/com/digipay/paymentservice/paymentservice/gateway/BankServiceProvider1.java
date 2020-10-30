package com.digipay.paymentservice.paymentservice.gateway;

import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BankServiceProvider1 implements BankServiceProvider {

    private static final String PAYMENT_URL = "http://localhost:9090/payments/transfer";
    private final RestTemplate restTemplate;

    @Override
    public PaymentProcessorResponse transfer(
            final PaymentDetails paymentDetails) {

        return restTemplate.postForObject(PAYMENT_URL, paymentDetails, PaymentProcessorResponse.class);
    }
}
