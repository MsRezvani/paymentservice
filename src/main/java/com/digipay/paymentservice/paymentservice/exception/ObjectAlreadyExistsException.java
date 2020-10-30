package com.digipay.paymentservice.paymentservice.exception;

public class ObjectAlreadyExistsException extends RuntimeException {

    public ObjectAlreadyExistsException(String message) {

        super(message);
    }
}