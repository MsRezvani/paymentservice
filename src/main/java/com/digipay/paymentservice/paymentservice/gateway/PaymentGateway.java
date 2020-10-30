package com.digipay.paymentservice.paymentservice.gateway;

import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGateway {

    private final BankServiceProvider1 provider1;
    private final BankServiceProvider2 provider2;

    public PaymentProcessorResponse transfer(
            final PaymentDetails paymentDetails) {

        if (paymentDetails.getSource().startsWith("6037")) {
            return provider1.transfer(paymentDetails);
        } else {
            return provider2.transfer(paymentDetails);
        }
    }
}
