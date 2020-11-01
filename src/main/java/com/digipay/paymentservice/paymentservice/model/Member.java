package com.digipay.paymentservice.paymentservice.model;

import com.digipay.paymentservice.paymentservice.utils.NumericBooleanDeserializer;
import com.digipay.paymentservice.paymentservice.utils.NumericBooleanSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Positive
    @Min(value = 1_000_000_000)
    @Column(
            name = "nationality_code",
            unique = true,
            nullable = false,
            columnDefinition = "bigint(10)"
    )
    private Long nationalityCode;
    @Column(name = "active", nullable = false)
    @JsonProperty
    @JsonSerialize(using = NumericBooleanSerializer.class)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    private boolean active;
    @Positive
    @Column(
            name = "member_number",
            unique = true,
            nullable = false
    )
    private Long memberNumber;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "member")
    private List<Card> cardList;
}
