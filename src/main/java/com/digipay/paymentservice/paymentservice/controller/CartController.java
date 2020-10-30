package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.Service.CartService;
import com.digipay.paymentservice.paymentservice.exception.ValidationsException;
import com.digipay.paymentservice.paymentservice.model.Cart;
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
@RequestMapping(value = "/api/v1/members/{memberNumber}/carts",
        produces = {"application/json"}
)
public class CartController {

    private final CartService cartService;

    /**
     * return All Member's Carts
     *
     * @param memberNumber
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> getMemberCarts(
            @PathVariable("memberNumber") Long memberNumber) {

        return ResponseEntity.ok(cartService.getMemberCarts(memberNumber));
    }

    /**
     * get Specific Cart From Member
     *
     * @param memberNumber
     * @param cartNumber
     * @return
     */
    @GetMapping("/{cartNumber}")
    public ResponseEntity<Cart> getCartByCartNumber(
            @PathVariable("memberNumber") Long memberNumber,
            @PathVariable("cartNumber")
            @Size(min = 19, message = "CartNumber Format : ####-####-####-####")
                    String cartNumber
    ) {

        return ResponseEntity.ok(
                cartService.getCartByNumberAndMemberNumber(cartNumber, memberNumber)
        );
    }

    /**
     * add A Cart to Member's CartList
     *
     * @param memberNumber
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity addCart(
            @PathVariable("memberNumber") Long memberNumber,
            @Valid @RequestBody Cart cart,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new ValidationsException(result);
        }
        Cart        savedCart = cartService.create(memberNumber, cart);
        HttpHeaders headers   = new HttpHeaders();
        headers.add("Location",
                    "/api/v1/members/" + memberNumber + "/carts/" + savedCart.getCartNumber());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    /**
     * remove cart form Member's cartList
     *
     * @param memberNumber
     * @param cartNumber
     * @return
     */
    @DeleteMapping("/{cartNumber}")
    public ResponseEntity remove(
            @PathVariable("memberNumber") Long memberNumber,
            @PathVariable("cartNumber") String cartNumber) {

        cartService.remove(memberNumber, cartNumber);
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

        PaymentProcessorResponse response = cartService.transfer(memberNumber, paymentDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


