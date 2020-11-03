package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.PaymentTransaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {

    PaymentTransaction save(PaymentTransaction transaction);
}
