package com.digipay.paymentservice.paymentservice.gateway;

import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import org.springframework.stereotype.Service;

@Service
public interface BankServiceProvider {
    PaymentProcessorResponse transfer(final PaymentDetails paymentDetails);
}
