package com.digipay.paymentservice.paymentservice.Service;

import com.digipay.paymentservice.paymentservice.exception.ObjectAlreadyExistsException;
import com.digipay.paymentservice.paymentservice.exception.ResourceNotFoundException;
import com.digipay.paymentservice.paymentservice.gateway.PaymentGateway;
import com.digipay.paymentservice.paymentservice.model.Cart;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.notification.NotificationService;
import com.digipay.paymentservice.paymentservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final MemberService memberService;
    private final TransactionService transactionService;
    private final RestTemplate restTemplate;
    private final PaymentGateway gateway;
    private final NotificationService notificationService;

    /**
     * Return All Member's Cart
     *
     * @param memberNumber
     * @return
     */
    public List<Cart> getMemberCarts(Long memberNumber) {

        checkMemberExists(memberNumber);
        return cartRepository.findByMember_MemberNumber(memberNumber)
                             .orElseThrow(() -> new ResourceNotFoundException("Member doesn't have any cart."));
    }


    public Cart getCartByNumberAndMemberNumber(String cartNumber,
                                               Long memberNumber) {

        checkMemberExists(memberNumber);
        return cartRepository
                .findByCartNumberAndMember_MemberNumber(cartNumber, memberNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Member doesn't have any Cart with Number : " + cartNumber));
    }

    /**
     * save Cart & Add to the Member's CartList
     *
     * @param memberNumber
     * @param cart
     * @return
     */
    @Transactional
    public Cart create(Long memberNumber, Cart cart) {

        if (cartRepository.existsByCartNumber(cart.getCartNumber())) {
            throw new ObjectAlreadyExistsException(
                    "Cart with Number : " + cart.getCartNumber() + " Already Exists.");
        }
        final Member member = memberService.findByMemberNumber(memberNumber);
        cart.setMember(member);
        member.getCartList()
              .add(cart);
        final Cart savedCard = cartRepository.save(cart);
        return savedCard;
    }

    /**
     * Delete Cart From Account 'CartList' & itself
     *
     * @param memberNumber
     * @param cartNumber
     */
    @Transactional
    public void remove(Long memberNumber, String cartNumber) {

        final Member member = memberService.findByMemberNumber(memberNumber);
        final Cart   cart   = getCartByNumberAndMemberNumber(cartNumber, memberNumber);
        member.getCartList()
              .remove(cart);
        cartRepository.delete(cart);
    }

    /**
     * Transfer Money from Cart to another Cart
     *
     * @param memberNumber   of Owner's Cart
     * @param paymentDetails have all information that need to transfer
     * @return
     */
    @Transactional
    public PaymentProcessorResponse transfer(Long memberNumber,
                                             final PaymentDetails paymentDetails) {

        checkMemberExists(memberNumber);
        Cart sourceCart = getCartByNumberAndMemberNumber(paymentDetails.getSource(), memberNumber);

        final PaymentProcessorResponse response = gateway.transfer(paymentDetails);
        if (response.getPaymentResponseStatus() == PaymentProcessorResponse.PaymentResponseStatus.SUCCESS) {
            sendNotification(paymentDetails.getSource(), paymentDetails.getAmount(), new Date());
        }
        transactionService.save(sourceCart, paymentDetails, response);
        return response;
    }

    private void sendNotification(String dest, BigDecimal amount, Date date) {

        notificationService.notify(dest, amount, date);
    }

    /**
     * private Method to check a member Exists or Not
     *
     * @param memberNumber
     */
    private void checkMemberExists(Long memberNumber) {

        if (!memberService.existsMember(memberNumber))
            throw new ResourceNotFoundException("Member with Number : " + memberNumber + " does not exist.");
    }
}
