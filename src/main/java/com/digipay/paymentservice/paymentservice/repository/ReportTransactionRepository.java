package com.digipay.paymentservice.paymentservice.repository;

import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

    Optional<List<PaymentTransaction>> findByCardAndTransactionDateBetween(
            Card card, Integer from, Integer to);
}
