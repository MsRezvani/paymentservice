package com.digipay.paymentservice.paymentservice.Service;

import com.digipay.paymentservice.paymentservice.exception.ResourceNotFoundException;
import com.digipay.paymentservice.paymentservice.model.Member;
import com.digipay.paymentservice.paymentservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    public final MemberRepository memberRepository;

    public Boolean existsMember(Long memberNumber) {
        return memberRepository.existsByMemberNumber(memberNumber);
    }

    public Member findByMemberNumber(Long memberNumber) {
        return memberRepository.findByMemberNumber(memberNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Member with Number : " + memberNumber + " does not exist."));
    }

    public Member create(Member member) {
        return memberRepository.save(member);
    }
}
