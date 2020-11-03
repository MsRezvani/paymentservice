package com.digipay.paymentservice.paymentservice.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(ValidationsException.class)
    public ResponseEntity<ExceptionResponseDTO> validationExceptionHandler(
            ValidationsException exception) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        "Error in Data Entry",
                        new Date(),
                        exception.bindingResult
                                .getFieldErrors()
                                .stream()
                                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                                .collect(Collectors.toList())
                )
                , HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponseDTO> noSuchElementExceptionHandler(
            NoSuchElementException ex) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        ex.getMessage(),
                        new Date(),
                        new ArrayList<>()
                )
                , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> noSuchElementExceptionHandler(
            IllegalArgumentException ex) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        ex.getMessage(),
                        new Date(),
                        new ArrayList<>()
                )
                , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> dataIntegrityViolationExceptionHandler(
            DataIntegrityViolationException exception) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        exception.getMessage(),
                        new Date(),
                        new ArrayList<>()
                )
                , HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> paymentFailedExceptionHandler(
            ConstraintViolationException ex) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        ex.getMessage().substring(ex.getMessage().indexOf(":") + 2),
                        new Date(),
                        new ArrayList<>()
                )
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ExceptionResponseDTO> paymentFailedExceptionHandler(
            PaymentFailedException ex) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "Payment FAILED",
                        ex.response.getDescription(),
                        new Date(),
                        Arrays.asList("Payment Id : " + ex.response.getPaymentId())
                )
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> objectAlreadyExistsExceptionHandler(
            ObjectAlreadyExistsException ex) {

        return new ResponseEntity<>(
                new ExceptionResponseDTO(
                        "fail",
                        ex.getMessage(),
                        new Date(),
                        Arrays.asList()
                )
                , HttpStatus.BAD_REQUEST);
    }
}
