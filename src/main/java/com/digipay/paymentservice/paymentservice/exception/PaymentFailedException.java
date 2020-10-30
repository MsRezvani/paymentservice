package com.digipay.paymentservice.paymentservice.exception;

import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;


public class PaymentFailedException extends RuntimeException {
    final PaymentProcessorResponse response;

    public PaymentFailedException(PaymentProcessorResponse response) {
        super("Payment Exception");
        this.response = response;
    }
}