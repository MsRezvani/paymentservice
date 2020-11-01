package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.model.Member;

public interface IMemberService {

    Boolean existsMember(Long memberNumber);

    Member findByMemberNumber(Long memberNumber);

    Member create(Member member);
}
