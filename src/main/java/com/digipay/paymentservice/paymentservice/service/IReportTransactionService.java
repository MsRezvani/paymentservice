package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IReportTransactionService {


    public Map<PaymentProcessorResponse.PaymentResponseStatus, List<Transaction>> getReport(
            Long memberNumber,
            String cartNumber,
            Integer from, Integer to);
}
