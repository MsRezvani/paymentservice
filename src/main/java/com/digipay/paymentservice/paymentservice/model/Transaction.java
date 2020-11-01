package com.digipay.paymentservice.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_transaction")
public class Transaction {

    @Column(name = "payment_id", unique = true, nullable = false)
    private Long paymentId;
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "transaction_code", unique = true, nullable = false)
    private UUID transactionCode;
    @Column(name = "destination_card_number", nullable = false)
    private String destinationCardNumber;
    @Column(name = "transaction_date", nullable = false)
    private Integer transactionDate;
    @Column(name = "amount_transaction", nullable = false)
    private BigDecimal amountTransaction;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentProcessorResponse.PaymentResponseStatus result;
    @JsonIgnore
    @ManyToOne
    private Card card;

    public Transaction(Card sourceCard,
                       PaymentDetails details,
                       PaymentProcessorResponse response
    ) {

        LocalDate localDate = LocalDate.now();
        this.transactionCode   = UUID.randomUUID();
        this.transactionDate   = Integer.valueOf(String.valueOf(localDate.getYear()) +
                                                             String.valueOf(localDate.getMonthValue()) +
                                                             String.valueOf(localDate.getDayOfMonth()));
        this.amountTransaction = details.getAmount();
        this.description           = response.getDescription();
        this.destinationCardNumber = details.getDest();
        this.paymentId             = response.getPaymentId();
        this.result            = response.getPaymentResponseStatus();
        this.card              = sourceCard;

    }

}
