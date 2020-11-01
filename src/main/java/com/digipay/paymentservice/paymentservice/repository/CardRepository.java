package com.digipay.paymentservice.paymentservice.repository;

import com.digipay.paymentservice.paymentservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumberAndMember_MemberNumber(String cardNumber,
                                                          Long memberNumber);

    Optional<List<Card>> findByMember_MemberNumber(Long memberNumber);

    @Query("select case when count(c.id)> 0 then true else false end from Card c where c.cardNumber = :cardNumber")
    Boolean existsByCardNumber(@Param("cardNumber") String cardNumber);
}

