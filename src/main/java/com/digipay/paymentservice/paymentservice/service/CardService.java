package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.exception.ObjectAlreadyExistsException;
import com.digipay.paymentservice.paymentservice.gateway.PaymentGateway;
import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final IMemberService memberService;
    private final ITransactionService transactionService;
    private final RestTemplate restTemplate;
    private final PaymentGateway gateway;
    private final INotificationService notificationService;

    /**
     * Return All Member's Card
     *
     * @param memberNumber
     * @return
     */
    public List<Card> getMemberCards(Long memberNumber) {

        checkMemberExists(memberNumber);
        return cardRepository.findByMember_MemberNumber(memberNumber)
                             .orElseThrow(() -> new NoSuchElementException("Member doesn't have any card."));
    }


    public Card getCardByNumberAndMemberNumber(String cardNumber,
                                               Long memberNumber) {

        checkMemberExists(memberNumber);
        return cardRepository
                .findByCardNumberAndMember_MemberNumber(cardNumber, memberNumber)
                .orElseThrow(() -> new NoSuchElementException("Member doesn't have any Card with Number : " + cardNumber));
    }

    /**
     * save Card & Add to the Member's CardList
     *
     * @param memberNumber
     * @param card
     * @return
     */
    @Transactional
    public Card create(Long memberNumber, Card card) {

        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new ObjectAlreadyExistsException(
                    "Card with Number : " + card.getCardNumber() + " Already Exists.");
        }
        final Member member = memberService.findByMemberNumber(memberNumber);
        card.setMember(member);
        member.getCardList()
              .add(card);
        final Card savedCard = cardRepository.save(card);
        return savedCard;
    }

    /**
     * Delete Card From Account 'CardList' & itself
     *
     * @param memberNumber
     * @param cardNumber
     */
    @Transactional
    public void remove(Long memberNumber, String cardNumber) {

        final Member member = memberService.findByMemberNumber(memberNumber);
        final Card   card   = getCardByNumberAndMemberNumber(cardNumber, memberNumber);
        member.getCardList()
              .remove(card);
        cardRepository.delete(card);
    }

    /**
     * Transfer Money from Card to another Card
     *
     * @param memberNumber   of Owner's Card
     * @param paymentDetails have all information that need to transfer
     * @return
     */
    @Transactional
    public PaymentProcessorResponse transfer(Long memberNumber,
                                             final PaymentDetails paymentDetails) {

        checkMemberExists(memberNumber);
        Card sourceCard = getCardByNumberAndMemberNumber(paymentDetails.getSource(), memberNumber);
        final PaymentProcessorResponse response = gateway.transfer(paymentDetails);
        if (response.getPaymentResponseStatus() == PaymentProcessorResponse.PaymentResponseStatus.SUCCESS) {
            sendNotification(paymentDetails.getSource(), paymentDetails.getAmount(), new Date());
        }
        transactionService.save(sourceCard, paymentDetails, response);
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
            throw new NoSuchElementException("Member with Number : " + memberNumber + " does not exist.");
    }
}
