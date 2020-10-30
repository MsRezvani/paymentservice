package com.digipay.paymentservice.paymentservice.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class Message implements Serializable {

    static final long serialVersionUID = 356341354131L;
    @JsonProperty("dest")
    String dest;
    @JsonProperty("amount")
    BigDecimal amount;
    @JsonProperty("date")
    Date date;

    @JsonCreator
    public Message(
            @JsonProperty("dest") String dest,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("date") Date date) {

        this.dest   = dest;
        this.amount = amount;
        this.date   = date;
    }
}
