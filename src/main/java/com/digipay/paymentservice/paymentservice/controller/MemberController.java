package com.digipay.paymentservice.paymentservice.controller;

import com.digipay.paymentservice.paymentservice.Service.MemberService;
import com.digipay.paymentservice.paymentservice.exception.ValidationsException;
import com.digipay.paymentservice.paymentservice.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberNumber}")
    public Member find(@PathVariable("memberNumber") Long memberNumber) {
        return memberService
                .findByMemberNumber(memberNumber);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Member member,
                                    BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationsException(result);
        }
        Member savedMember = memberService.create(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/persons" + savedMember.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

//    @PutMapping
//    public ResponseEntity<Member> update(Long code, @RequestBody Member member) {
//        return ResponseEntity.ok(memberService.update(code, member));
//    }

}
