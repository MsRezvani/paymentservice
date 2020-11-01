package com.digipay.paymentservice.paymentservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.digipay.paymentservice.paymentservice.controller.utils.generator.generateNumber;
import static com.digipay.paymentservice.paymentservice.controller.utils.generator.generateStatus;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest extends CartControllerTestFixture {

    @Test
    @DisplayName("getMemberCarts->Member Not Exists")
//    @Disabled
    void getMemberCarts_MemberNotExists() throws Exception {

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
    @DisplayName("getMemberCarts->Member doesn't Have any Cart")
//    @Disabled
    void getMemberCarts_dosenotHaveAnyCart() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + member2.getMemberNumber() + CART_PREFIX_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any cart.")));
    }

    @Test
    @DisplayName("getMemberCarts->Member Have 3 Cart.")
//    @Disabled
    void getMemberCarts_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL + member1.getMemberNumber() + CART_PREFIX_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.*", hasSize(4)))
               .andExpect(jsonPath("$.[*].cartNumber",
                                   hasItems(cart1.getCartNumber(),
                                            cart2.getCartNumber(),
                                            cart3.getCartNumber(),
                                            cart4.getCartNumber())));


    }


    @Test
    @DisplayName("getCartByCartNumber->CartNotExists")
//    @Disabled
    void getCartByCartNumber_CartNotExists() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL +
                             member1.getMemberNumber() +
                             CART_PREFIX_URL +
                             "/" +
                             cart5.getCartNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any Cart with Number : " + cart5.getCartNumber())));
    }


    @Test
    @DisplayName("getCartByCartNumber->Member Have this card")
//    @Disabled
    void getCartByCartNumber_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get(BASE_URL +
                             member1.getMemberNumber() +
                             CART_PREFIX_URL +
                             "/" +
                             cart3.getCartNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.ccv2", is(cart3.getCcv2())))
               .andExpect(jsonPath("$.expDate", is(cart3.getExpDate())))
               .andExpect(jsonPath("$.cartNumber", is(cart3.getCartNumber())))
               .andExpect(jsonPath("$.pin", is(cart3.getPin().intValue())));
    }


    @Test
    @DisplayName("AddCart->Card Already Exists")
//    @Disabled
    void addCart_CartAlreadyExists() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL)
                .contentType("application/json")
                .content("{\n" +
                                 "    \"cartNumber\": \"" + cart1.getCartNumber() + "\",\n" +
                                 "    \"ccv2\": " + cart1.getCcv2() + ",\n" +
                                 "    \"expDate\": " + cart1.getExpDate() + ",\n" +
                                 "    \"pin\": " + cart1.getPin() + ",\n" +
                                 "    \"active\": \"" + cart1.getActive() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Cart with Number : " + cart1.getCartNumber() + " Already Exists.")));
    }


    @Test
    @DisplayName("AddCart-> Valid Input")
//    @Disabled
    void addCart_ValidInput() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post(BASE_URL +
                              member1.getMemberNumber() +
                              CART_PREFIX_URL)
                .contentType("application/json")
                .content("{\n" +
                                 "    \"cartNumber\": \"" + cart4.getCartNumber() + "\",\n" +
                                 "    \"ccv2\": " + cart4.getCcv2() + ",\n" +
                                 "    \"expDate\": " + cart4.getExpDate() + ",\n" +
                                 "    \"pin\": " + cart4.getPin() + ",\n" +
                                 "    \"active\": \"" + cart4.getActive() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
               .andExpect(status().isCreated())
               .andExpect(header().string("Location",
                                          "/api/v1/members/" + member1.getMemberNumber() + "/carts/" + cart4.getCartNumber()));
    }

    @Test
    @Disabled
    void transfer() throws Exception {

        Long   paymentId = generateNumber();
        String status    = "SUCCESS";
        String message   = "Transaction is Done.";
        WireMock.stubFor(
                WireMock.post((cart1.getCartNumber().startsWith("6037") ? "/payments/transfer" : "/cards/pay"))
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
                                 "    \"source\": \"" + cart1.getCartNumber() + "\",\n" +
                                 "    \"dest\": \"" + cart4.getCartNumber() + "\",\n" +
                                 "    \"ccv2\": " + cart1.getCcv2() + ",\n" +
                                 "    \"expDate\": \"" + cart1.getExpDate() + "\",\n" +
                                 "    \"pin\": " + cart1.getPin() + ",\n" +
                                 "    \"amount\": \"" + generateNumber() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.paymentId", is(String.valueOf(paymentId.intValue()))))
               .andExpect(jsonPath("$.paymentResponseStatus", is(status)))
               .andExpect(jsonPath("$.description", is(message)));
    }


    @Test
    @Disabled
    void transfer_RandomStatus() throws Exception {

        Long   paymentId = generateNumber();
        String status    = generateStatus();
        String message   = status.equals("SUCCESS") ? "Transaction is Done." : "Transaction is FAILED.";
        WireMock.stubFor(
                WireMock.post((cart1.getCartNumber().startsWith("6037") ? "/payments/transfer" : "/cards/pay"))
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
                                 "    \"source\": \"" + cart1.getCartNumber() + "\",\n" +
                                 "    \"dest\": \"" + cart4.getCartNumber() + "\",\n" +
                                 "    \"ccv2\": " + cart1.getCcv2() + ",\n" +
                                 "    \"expDate\": \"" + cart1.getExpDate() + "\",\n" +
                                 "    \"pin\": " + cart1.getPin() + ",\n" +
                                 "    \"amount\": \"" + generateNumber() + "\"\n" +
                                 "}")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.paymentId", is(String.valueOf(paymentId.intValue()))))
               .andExpect(jsonPath("$.paymentResponseStatus", is(status)))
               .andExpect(jsonPath("$.description", is(message)));
    }

    @AfterAll
    public void tearDown() {

        wireMockServer.stop();
    }


    @Test
//    @DisplayName("AddCart-> Valid Input")
//    @Disabled
    void removeCart() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .delete(BASE_URL +
                                member1.getMemberNumber() +
                                CART_PREFIX_URL
                                + "/" + cart3.getCartNumber()
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
                             cart3.getCartNumber())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request1)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType("application/json"))
               .andExpect(jsonPath("$.status", is("fail")))
               .andExpect(jsonPath("$.shortMessage",
                                   is("Member doesn't have any Cart with Number : " + cart3.getCartNumber())));
        // dirty context not working. added the removed cart for other Test case.
        cartRepository.save(cart3);
    }
}





























