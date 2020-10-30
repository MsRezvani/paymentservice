package com.digipay.paymentservice.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentDetails implements Serializable {

    @JsonProperty("source")
    String source;
    @JsonProperty("dest")
    String dest;
    @JsonProperty("ccv2")
    Integer ccv2;
    @JsonProperty("expDate")
    String expDate;
    @JsonProperty("pin")
    Integer pin;
    @JsonProperty("amount")
    BigDecimal amount;

    @JsonCreator
    public PaymentDetails(@JsonProperty("source") String source,
                          @JsonProperty("dest") String dest,
                          @JsonProperty("ccv2") Integer ccv2,
                          @JsonProperty("expDate") String expDate,
                          @JsonProperty("pin") Integer pin,
                          @JsonProperty("amount") BigDecimal amount) {

        this.source  = source;
        this.dest    = dest;
        this.ccv2    = ccv2;
        this.expDate = expDate;
        this.pin     = pin;
        this.amount  = amount;
    }
}
