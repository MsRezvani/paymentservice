package com.digipay.paymentservice.paymentservice.Service;

import com.digipay.paymentservice.paymentservice.model.Cart;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;

import java.util.List;

public interface ICardService {

    List<Cart> getMemberCarts(Long memberNumber);

    Cart getCartByNumberAndMemberNumber(String cartNumber,
                                        Long memberNumber);

    Cart create(Long memberNumber, Cart cart);

    void remove(Long memberNumber, String cartNumber);

    PaymentProcessorResponse transfer(Long memberNumber,
                                             final PaymentDetails paymentDetails);
}
