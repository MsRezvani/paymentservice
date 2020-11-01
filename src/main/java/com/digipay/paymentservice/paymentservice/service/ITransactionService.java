package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {

    public Transaction save(Card sourceCard, PaymentDetails details,
                            PaymentProcessorResponse response);
}
