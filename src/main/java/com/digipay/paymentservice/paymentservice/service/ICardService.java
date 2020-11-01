package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;

import java.util.List;

public interface ICardService {

    List<Card> getMemberCards(Long memberNumber);

    Card getCardByNumberAndMemberNumber(String cartNumber,
                                        Long memberNumber);

    Card create(Long memberNumber, Card card);

    void remove(Long memberNumber, String cartNumber);

    PaymentProcessorResponse transfer(Long memberNumber,
                                             final PaymentDetails paymentDetails);
}
