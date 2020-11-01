package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.PaymentServiceApplication;
import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.repository.CardRepository;
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
public class CardControllerTestFixture {

    static final String BASE_URL = "/api/v1/members/";
    static final String CART_PREFIX_URL = "/cards";
    Member member1;
    Member member2;
    Card card1;
    Card card2;
    Card card3;
    Card card4;
    Card card5;


    @Autowired CardRepository cardRepository;
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

        card1 = Card.builder()
                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(9902)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        card2 = Card.builder()

                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(9901)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        card3 = Card.builder()

                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2001)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        member1.setCardList(Arrays.asList(card1, card2, card3));
        card4 = Card.builder()
                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2010)
                    .pin(generateNumber())
                    .member(null)
                    .build();
        card5 = Card.builder()
                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2110)
                    .pin(generateNumber())
                    .member(null)
                    .build();
        cardRepository.save(card1);
        cardRepository.save(card2);
        cardRepository.save(card3);
        mockMvc        = MockMvcBuilders.webAppContextSetup(context).build();
        wireMockServer = new WireMockServer(9090);
        WireMock.configureFor("localhost", 9090);
        wireMockServer.start();
    }
}
