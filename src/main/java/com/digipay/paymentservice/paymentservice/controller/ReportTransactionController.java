package com.digipay.paymentservice.paymentservice.controller;


import com.digipay.paymentservice.paymentservice.Service.ReportTransactionService;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/members/{memberNumber}/carts/{cartNumber}/reports",
        produces = {"application/json"}
)
public class ReportTransactionController {


    private final ReportTransactionService reportsService;

    @GetMapping
    public ResponseEntity<Map<PaymentProcessorResponse.PaymentResponseStatus, List<Transaction>>> getMemberCarts(
            @PathVariable("memberNumber") Long memberNumber,
            @PathVariable("cartNumber") String cartNumber,
            @RequestParam("from") Integer from,
            @RequestParam("to") Integer to
    ) {

        return new ResponseEntity<>(
                reportsService.getReport(memberNumber, cartNumber, from, to),
                HttpStatus.OK
        );
    }

}