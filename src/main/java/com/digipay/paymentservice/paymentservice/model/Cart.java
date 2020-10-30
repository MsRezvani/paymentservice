package com.digipay.paymentservice.paymentservice.model;

import com.digipay.paymentservice.paymentservice.utils.NumericBooleanDeserializer;
import com.digipay.paymentservice.paymentservice.utils.NumericBooleanSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "member")
@Entity
@Table(name = "tbl_cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "cart_number", unique = true)
    @NotBlank
    @Size(min = 19, max = 19
            , message = "cart Number Format ####-####-####-####")
    private String cartNumber;
    @Min(value = 100)
    private Integer ccv2;
    @Positive
    private Integer expDate;
    @Positive
    private Long pin;
    @JsonProperty
    @JsonSerialize(using = NumericBooleanSerializer.class)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    private Boolean active;
    @JsonIgnore
    @ManyToOne
    private Member member;
}







