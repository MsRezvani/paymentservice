package com.digipay.paymentservice.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PaymentProcessorResponse {

    @JsonProperty("paymentId")
    Long paymentId;
    @JsonProperty("paymentResponseStatus")
    PaymentResponseStatus paymentResponseStatus;
    @JsonProperty("description")
    String description;

    @JsonCreator
    public PaymentProcessorResponse(
            @JsonProperty("paymentId") Long paymentId,
            @JsonProperty("paymentResponseStatus") String paymentResponseStatus,
            @JsonProperty("description") String description) {

        this.paymentId             = paymentId;
        this.paymentResponseStatus = paymentResponseStatus.equals("SUCCESS") ?
                                     PaymentResponseStatus.SUCCESS : PaymentResponseStatus.FAILED;
        this.description           = description;
    }

    public enum PaymentResponseStatus {
        SUCCESS, FAILED
    }
}