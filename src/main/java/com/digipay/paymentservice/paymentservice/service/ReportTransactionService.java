package com.digipay.paymentservice.paymentservice.Service;

import com.digipay.paymentservice.paymentservice.exception.ResourceNotFoundException;
import com.digipay.paymentservice.paymentservice.model.Cart;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.Transaction;
import com.digipay.paymentservice.paymentservice.repository.ReportTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportTransactionService {

    private final ReportTransactionRepository reportTransactionRepository;
    private final ICardService cartService;
    private final MemberService memberService;

    public Map<PaymentProcessorResponse.PaymentResponseStatus, List<Transaction>> getReport(
            Long memberNumber,
            String cartNumber,
            Integer from, Integer to) {

        memberService.findByMemberNumber(memberNumber);
        Cart memberCarts = cartService.getCartByNumberAndMemberNumber(cartNumber, memberNumber);
        List<Transaction> transactionList =
                reportTransactionRepository.findByCartAndTransactionDateBetween(memberCarts, from, to)
                                           .orElseThrow(() -> new ResourceNotFoundException("Member with Number : "
                                                                                                    + memberNumber +
                                                                                                    " Haven't any Transaction on Cart with Number : " + cartNumber));
        return transactionList.stream()
                              .collect(Collectors.groupingBy(Transaction::getResult));
    }
}
