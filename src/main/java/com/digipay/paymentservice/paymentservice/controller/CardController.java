package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.service.ICardService;
import com.digipay.paymentservice.paymentservice.exception.ValidationsException;
import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.PaymentDetails;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/members/{memberNumber}/cards",
        produces = {"application/json"}
)
public class CardController {

    private final ICardService cardService;

    /**
     * return All Member's Carts
     *
     * @param memberNumber
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Card>> getMemberCarts(
            @PathVariable("memberNumber") Long memberNumber) {

        return ResponseEntity.ok(cardService.getMemberCards(memberNumber));
    }

    /**
     * get Specific Cart From Member
     *
     * @param memberNumber
     * @param cardNumber
     * @return
     */
    @GetMapping("/{cardNumber}")
    public ResponseEntity<Card> getCartByCartNumber(
            @PathVariable("memberNumber") Long memberNumber,
            @PathVariable("cardNumber")
            @Size(min = 19, message = "CartNumber Format : ####-####-####-####")
                    String cardNumber
    ) {

        return ResponseEntity.ok(
                cardService.getCardByNumberAndMemberNumber(cardNumber, memberNumber)
        );
    }

    /**
     * add A Cart to Member's CartList
     *
     * @param memberNumber
     * @param card
     * @return
     */
    @PostMapping
    public ResponseEntity addCart(
            @PathVariable("memberNumber") Long memberNumber,
            @Valid @RequestBody Card card,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new ValidationsException(result);
        }
        Card        savedCard = cardService.create(memberNumber, card);
        HttpHeaders headers   = new HttpHeaders();
        headers.add("Location",
                    "/api/v1/members/" + memberNumber + "/cards/" + savedCard.getCardNumber());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    /**
     * remove card form Member's cardList
     *
     * @param memberNumber
     * @param cardNumber
     * @return
     */
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity remove(
            @PathVariable("memberNumber") Long memberNumber,
            @PathVariable("cardNumber") String cardNumber) {

        cardService.remove(memberNumber, cardNumber);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Transfer money from Cart to another
     *
     * @param memberNumber
     * @param paymentDetails
     * @return
     */
    @PostMapping("/transfer")
    public ResponseEntity<PaymentProcessorResponse> transfer(
            @PathVariable("memberNumber") Long memberNumber,
            @RequestBody PaymentDetails paymentDetails
    ) {

        PaymentProcessorResponse response = cardService.transfer(memberNumber, paymentDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


