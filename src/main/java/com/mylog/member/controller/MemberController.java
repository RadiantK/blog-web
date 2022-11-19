package com.mylog.member.controller;

import com.mylog.member.domain.GenderType;
import com.mylog.member.dto.MemberJoinRequest;
import com.mylog.member.dto.MemberLoginRequest;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.member.exception.DuplicatedMemberException;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.exception.WrongPasswordException;
import com.mylog.member.service.MemberLoginService;
import com.mylog.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final MemberLoginService memberLoginService;
    private final String LOGIN_MEMBER = "loginMember";

    @ModelAttribute("genders")
    public GenderType[] genderTypes() {
        return GenderType.values();
    }

    @GetMapping("/join/agree")
    public String joinAgreePage() {
        return "auth/join-agree";
    }

    @GetMapping("/join/main")
    public String joinMainPage(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequest());
        return "auth/join-main";
    }

    @PostMapping("/join/main")
    public String joinMain(@Validated @ModelAttribute MemberJoinRequest memberJoinRequest,
                           BindingResult bindingResult) {

        if (memberJoinRequest.isPasswordEqualToPasswordConfirm() == false) {
            bindingResult.addError(new FieldError("memberJoinRequest", "passwordConfirm", "비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {}", bindingResult);
            return "auth/join-main";
        }

        try {
            memberService.join(memberJoinRequest);
        } catch (DuplicatedMemberException e) {
            bindingResult.addError(new FieldError("memberJoinRequest", "email", "중복된 이메일이 존재합니다."));
        }

        return "redirect:/user/join/success";
    }

    @GetMapping("/join/success")
    public String joinSuccessPage(Model model) {
        model.addAttribute("member", new MemberJoinRequest());
        model.addAttribute("genderTypes", GenderType.values());
        return "auth/join-success";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("memberLoginRequest", new MemberLoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(MemberLoginRequest memberLoginRequest,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String url,
                        HttpServletRequest request) {

        log.info("memberLoginRequest : {}", memberLoginRequest);

        MemberLoginResponse memberLoginResponse;
        try {
            memberLoginResponse = memberLoginService.login(memberLoginRequest);
        } catch(MemberNotFoundException | WrongPasswordException e) {
            bindingResult.addError(new ObjectError("memberLoginRequest", "아이디 또는 비밀번호가 일치하지 않습니다."));
            return "auth/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, memberLoginResponse);

        return "redirect:" + url;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션이 있으면 세션 생성 x
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/help/id")
    public String findIdPage() {
        return "auth/find-id";
    }

    @GetMapping("/help/pwd")
    public String findPwdPage() {
        return "auth/find-pwd";
    }

    @DeleteMapping("/withdrawal/{email}")
    public String deleteMember(@PathVariable String email) {
        memberService.deleteMember(email);

        return "redirect:/";
    }
}
