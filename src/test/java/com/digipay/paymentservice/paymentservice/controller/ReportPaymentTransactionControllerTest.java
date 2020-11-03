package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.PaymentServiceApplication;
import com.digipay.paymentservice.paymentservice.service.ICardService;
import com.digipay.paymentservice.paymentservice.service.MemberService;
import com.digipay.paymentservice.paymentservice.model.Card;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.model.PaymentProcessorResponse;
import com.digipay.paymentservice.paymentservice.model.PaymentTransaction;
import com.digipay.paymentservice.paymentservice.repository.TransactionRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static com.digipay.paymentservice.paymentservice.controller.utils.generator.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PaymentServiceApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportPaymentTransactionControllerTest {

    private static final String BASE = "/api/v1/members/";
    private static final String CART = "/carts/";
    private static final String REPORT = "/reports";
    Member member1;
    Card card1;
    PaymentTransaction t1;
    PaymentTransaction t2;
    @Autowired ICardService cartService;
    @Autowired MemberService memberService;
    @Autowired TransactionRepository transactionRepository;
    private MockMvc mockMvc;
    private WireMockServer wireMockServer;

    @Autowired
    private WebApplicationContext context;


    @BeforeAll
    void setup() {

        member1 = Member.builder()
                        .firstName("mohsen")
                        .lastName("mazinani")
                        .memberNumber(generateNumber())
                        .active(true)
                        .nationalityCode(generateNationalityCode())
                        .build();
        card1 = Card.builder()
                    .cardNumber(generateCardNumbers())
                    .active(true)
                    .ccv2(generateCvv2())
                    .expDate(2010)
                    .pin(generateNumber())
                    .member(member1)
                    .build();
        t1    = PaymentTransaction.builder()
                                  .card(card1)
                                  .result(PaymentProcessorResponse.PaymentResponseStatus.SUCCESS)
                                  .amountTransaction(new BigDecimal(2500))
                                  .description("Transaction is Completed.")
                                  .paymentId(generateNumber())
                                  .transactionDate(990203)
                                  .destinationCardNumber(generateCardNumbers())
                                  .build();
        t2    = PaymentTransaction.builder()
                                  .card(card1)
                                  .result(PaymentProcessorResponse.PaymentResponseStatus.FAILED)
                                  .amountTransaction(new BigDecimal(generateNumber()))
                                  .description("Transaction is Failed.")
                                  .paymentId(generateNumber())
                                  .transactionDate(990203)
                                  .destinationCardNumber(generateCardNumbers())
                                  .build();
        memberService.create(member1);
        cartService.create(member1.getMemberNumber(), card1);
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void report_withRecord() throws Exception {


        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE +
                             member1.getMemberNumber() +
                             CART +
                             card1.getCardNumber() +
                             REPORT)
                .param("from", String.valueOf(t1.getTransactionDate() - 1))
                .param("to", String.valueOf(t1.getTransactionDate() + 1))
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType("application/json"))
                                     .andExpect(jsonPath("$.*", hasSize(2)))
                                     .andExpect(jsonPath("$.SUCCESS.*", hasSize(1)))
                                     .andExpect(jsonPath("$.FAILED.*", hasSize(1)))
                                     .andExpect(jsonPath("$.SrabUCCESS[?(@.paymentId == '" + t1.getPaymentId() + "' )]").exists())
                                     .andExpect(jsonPath("$.FAILED[?(@.paymentId == '" + t2.getPaymentId() + "' )]").exists())
                                     .andReturn();
    }

    @Test
    void report_withoutRecord() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE +
                             member1.getMemberNumber() +
                             CART +
                             card1.getCardNumber() +
                             REPORT)
                .param("from", String.valueOf(t1.getTransactionDate() + 1000))
                .param("to", String.valueOf(t1.getTransactionDate() + 100000))
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member with Number : "
                                              + member1.getMemberNumber() +
                                              " Haven't any Transaction on Card with Number : " + card1.getCardNumber())));
    }
}