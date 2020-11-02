package com.digipay.paymentservice.paymentservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static com.digipay.paymentservice.paymentservice.controller.utils.generator.generateNumber;
import static com.digipay.paymentservice.paymentservice.controller.utils.generator.generateStatus;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest extends CardControllerTestFixture {

    @Test
    @DisplayName("getMemberCards->Member Not Exists")
//    @Disabled
    void getMemberCards_MemberNotExists() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + Long.MAX_VALUE + CART_PREFIX_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member with Number : " + Long.MAX_VALUE + " does not exist.")));

    }

    @Test
    @DisplayName("getMemberCards->Member doesn't Have any Card")
//    @Disabled
    void getMemberCards_dosenotHaveAnyCard() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + member2.getMemberNumber() + CART_PREFIX_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any card.")));
    }

    @Test
    @DisplayName("getMemberCards->Member Have 3 Card.")
//    @Disabled
    void getMemberCards_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + member1.getMemberNumber() + CART_PREFIX_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.*", hasSize(4)))
               .andExpect(jsonPath("$.[*].cardNumber",
                                   hasItems(card1.getCardNumber(),
                                            card2.getCardNumber(),
                                            card3.getCardNumber(),
                                            card4.getCardNumber())));


    }


    @Test
    @DisplayName("getCardByCardNumber->CardNotExists")
//    @Disabled
    void getCardByCardNumber_CardNotExists() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL +
                             member1.getMemberNumber() +
                             CART_PREFIX_URL +
                             "/" +
                             card5.getCardNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any Card with Number : " + card5.getCardNumber())));
    }


    @Test
    @DisplayName("getCardByCardNumber->Member Have this card")
//    @Disabled
    void getCardByCardNumber_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL +
                             member1.getMemberNumber() +
                             CART_PREFIX_URL +
                             "/" +
                             card3.getCardNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.ccv2", is(card3.getCcv2())))
               .andExpect(jsonPath("$.expDate", is(card3.getExpDate())))
               .andExpect(jsonPath("$.cardNumber", is(card3.getCardNumber())))
               .andExpect(jsonPath("$.pin", is(card3.getPin().intValue())));
    }


    @Test
    @DisplayName("AddCard->Card Already Exists")
//    @Disabled
    void addCard_CardAlreadyExists() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL)
                .contentType("application/json")
                .content("{\n" +
                                 "    \"cardNumber\": \"" + card1.getCardNumber() + "\",\n" +
                                 "    \"ccv2\": " + card1.getCcv2() + ",\n" +
                                 "    \"expDate\": " + card1.getExpDate() + ",\n" +
                                 "    \"pin\": " + card1.getPin() + ",\n" +
                                 "    \"active\": \"" + card1.getActive() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Card with Number : " + card1.getCardNumber() + " Already Exists.")));
    }


    @Test
    @DisplayName("AddCard-> Valid Input")
//    @Disabled
    void addCard_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL)
                .contentType("application/json")
                .content("{\n" +
                                 "    \"cardNumber\": \"" + card4.getCardNumber() + "\",\n" +
                                 "    \"ccv2\": " + card4.getCcv2() + ",\n" +
                                 "    \"expDate\": " + card4.getExpDate() + ",\n" +
                                 "    \"pin\": " + card4.getPin() + ",\n" +
                                 "    \"active\": \"" + card4.getActive() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isCreated())
               .andExpect(header().string("Location",
                                          "/api/v1/members/" + member1.getMemberNumber() + "/cards/" + card4.getCardNumber()));
    }

    @Test
//    @Disabled
    void transfer() throws Exception {

        Long   paymentId = generateNumber();
        String status    = "SUCCESS";
        String message   = "Transaction is Done.";
        WireMock.stubFor(
                WireMock.post((card1.getCardNumber().startsWith("6037") ? "/payments/transfer" : "/cards/pay"))
                        .willReturn(WireMock.okForContentType("application/json",
                                                              "{\n" +
                                                                      "    \"paymentId\": \"" + paymentId + "\"," +
                                                                      "    \"paymentResponseStatus\": \"" + status + "\"," +
                                                                      "    \"description\": \"" + message + "\"\n" +
                                                                      "}"))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL +
                              "/transfer")
                .contentType("application/json")
                .content("{\n" +
                                 "    \"source\": \"" + card1.getCardNumber() + "\",\n" +
                                 "    \"dest\": \"" + card4.getCardNumber() + "\",\n" +
                                 "    \"ccv2\": " + card1.getCcv2() + ",\n" +
                                 "    \"expDate\": \"" + card1.getExpDate() + "\",\n" +
                                 "    \"pin\": " + card1.getPin() + ",\n" +
                                 "    \"amount\": \"" + generateNumber() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.paymentId", is(paymentId.intValue())))
               .andExpect(jsonPath("$.paymentResponseStatus", is(status)))
               .andExpect(jsonPath("$.description", is(message)));
    }


    @Test
//    @Disabled
    void transfer_RandomStatus() throws Exception {

        Long   paymentId = generateNumber();
        String status    = generateStatus();
        String message   = status.equals("SUCCESS") ? "Transaction is Done." : "Transaction is FAILED.";
        WireMock.stubFor(
                WireMock.post((card1.getCardNumber().startsWith("6037") ? "/payments/transfer" : "/cards/pay"))
                        .willReturn(WireMock.okForContentType("application/json",
                                                              "{\n" +
                                                                      "    \"paymentId\": \"" + paymentId + "\"," +
                                                                      "    \"paymentResponseStatus\": \"" + status + "\"," +
                                                                      "    \"description\": \"" + message + "\"\n" +
                                                                      "}"))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL +
                              "/transfer")
                .contentType("application/json")
                .content("{\n" +
                                 "    \"source\": \"" + card1.getCardNumber() + "\",\n" +
                                 "    \"dest\": \"" + card4.getCardNumber() + "\",\n" +
                                 "    \"ccv2\": " + card1.getCcv2() + ",\n" +
                                 "    \"expDate\": \"" + card1.getExpDate() + "\",\n" +
                                 "    \"pin\": " + card1.getPin() + ",\n" +
                                 "    \"amount\": \"" + generateNumber() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.paymentId", is(paymentId.intValue())))
               .andExpect(jsonPath("$.paymentResponseStatus", is(status)))
               .andExpect(jsonPath("$.description", is(message)));
    }

    @AfterAll
    public void tearDown() {

        wireMockServer.stop();
    }


    @Test
//    @DisplayName("AddCard-> Valid Input")
//    @Disabled
    void removeCard() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL +
                                member1.getMemberNumber() +
                                CART_PREFIX_URL
                                + "/" + card3.getCardNumber()
                )
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isNoContent());
        RequestBuilder request1 = MockMvcRequestBuilders
                .get(BASE_URL +
                             member1.getMemberNumber() +
                             CART_PREFIX_URL +
                             "/" +
                             card3.getCardNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request1)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any Card with Number : " + card3.getCardNumber())));
        // dirty context not working. added the removed card for other Test case.
        cardRepository.save(card3);
    }
}





























