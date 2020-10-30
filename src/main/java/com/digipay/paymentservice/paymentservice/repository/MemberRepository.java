package com.digipay.paymentservice.paymentservice.repository;

import com.digipay.paymentservice.paymentservice.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberNumber(Long memberNumber);

    @Query("select case when count(mem.id)> 0 then true else false end from Member mem where mem.memberNumber = :memberNumber")
    Boolean existsByMemberNumber(@Param("memberNumber") Long memberNumber);
}
