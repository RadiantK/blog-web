package com.mylog.member.controller.api;

import com.mylog.member.domain.Member;
import com.mylog.member.service.MemberService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiMemberController {

    private final MemberService memberService;

    @GetMapping("/join/email/{email}")
    public CheckEmailResponse duplicatedMemberCheck(
            @PathVariable("email") String email
    ) {
        Member member = memberService.findMember(email);
        return member == null
                ? CheckEmailResponse.builder().result("use").build()
                : CheckEmailResponse.builder().result("duple").build();
    }

    @Data
    @Builder
    static class CheckEmailResponse {
        private String result;
    }
}
