package com.digipay.paymentservice.paymentservice.exception;

import org.springframework.validation.BindingResult;

public class ValidationsException extends RuntimeException {
    BindingResult bindingResult;

    public ValidationsException(BindingResult bindingResult) {
        super("Validation Exception");
        this.bindingResult = bindingResult;
    }
}