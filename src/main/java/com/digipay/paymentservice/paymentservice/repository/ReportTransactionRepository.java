package com.digipay.paymentservice.paymentservice.repository;

import com.digipay.paymentservice.paymentservice.model.Cart;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportTransactionRepository
        extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findByCartAndTransactionDateBetween(
            Cart cart, Integer from, Integer to);
}
