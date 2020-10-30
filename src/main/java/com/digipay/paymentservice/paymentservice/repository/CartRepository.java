package com.digipay.paymentservice.paymentservice.repository;

import com.digipay.paymentservice.paymentservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCartNumberAndMember_MemberNumber(String cartNumber,
                                                          Long memberNumber);

    Optional<List<Cart>> findByMember_MemberNumber(Long memberNumber);

    @Query("select case when count(c.id)> 0 then true else false end from Cart c where c.cartNumber = :cartNumber")
    Boolean existsByCartNumber(@Param("cartNumber") String cartNumber);
}

