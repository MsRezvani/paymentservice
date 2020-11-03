package com.digipay.paymentservice.paymentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ExceptionResponseDTO {
    String status;
    String shortMessage;
    Date date;
    List<String> errors;
}
