package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.PaymentServiceApplication;
import com.digipay.paymentservice.paymentservice.model.Cart;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.repository.CartRepository;
import com.digipay.paymentservice.paymentservice.repository.MemberRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.digipay.paymentservice.paymentservice.controller.utils.generator.*;

@SpringBootTest(classes = PaymentServiceApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerTestFixture {

    static final String BASE_URL = "/api/v1/members/";
    static final String CART_PREFIX_URL = "/carts";
    Member member1;
    Member member2;
    Cart cart1;
    Cart cart2;
    Cart cart3;
    Cart cart4;
    Cart cart5;


    @Autowired CartRepository cartRepository;
    @Autowired MemberRepository memberRepository;
    MockMvc mockMvc;
    WireMockServer wireMockServer;

    @Autowired
    WebApplicationContext context;

    @BeforeAll
    public void setup() {

        member1 = Member.builder()
                        .firstName("mostafa")
                        .lastName("mazinani")
                        .memberNumber(generateNumber())
                        .active(true)
                        .nationalityCode(generateNationalityCode())
                        .build();
        member2 = Member.builder()
                        .firstName("morteza")
                        .lastName("mazinani")
                        .memberNumber(generateNumber())
                        .active(true)
                        .nationalityCode(generateNationalityCode())
                        .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        cart1 = Cart.builder()
                    .cartNumber(generateCartNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(9902)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        cart2 = Cart.builder()

                    .cartNumber(generateCartNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(9901)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        cart3 = Cart.builder()

                    .cartNumber(generateCartNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2001)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        member1.setCartList(Arrays.asList(cart1, cart2, cart3));
        cart4 = Cart.builder()
                    .cartNumber(generateCartNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2010)
                    .pin(generateNumber())
                    .member(null)
                    .build();
        cart5 = Cart.builder()
                    .cartNumber(generateCartNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2110)
                    .pin(generateNumber())
                    .member(null)
                    .build();
        cartRepository.save(cart1);
        cartRepository.save(cart2);
        cartRepository.save(cart3);
        mockMvc        = MockMvcBuilders.webAppContextSetup(context).build();
        wireMockServer = new WireMockServer(9090);
        WireMock.configureFor("localhost", 9090);
        wireMockServer.start();
    }
}
