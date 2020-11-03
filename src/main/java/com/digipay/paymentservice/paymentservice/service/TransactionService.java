package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.PaymentTransaction;
import com.digipay.paymentservice.paymentservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    public PaymentTransaction save(PaymentTransaction transaction) {

        return transactionRepository.save(transaction);
    }
}
