package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import com.digipay.paymentservice.paymentservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction save(Card sourceCard, PaymentDetails details,
                            PaymentProcessorResponse response) {

        return transactionRepository.save(new Transaction(sourceCard, details, response));
    }
}
