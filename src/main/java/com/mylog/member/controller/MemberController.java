package com.mylog.member.controller;

import com.mylog.member.domain.GenderType;
import com.mylog.member.dto.MemberJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

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
//        model.addAttribute("genders", GenderType.values());
        return "auth/join-main";
    }

    @PostMapping("/join/main")
    public String joinMain(@Validated @ModelAttribute("memberJoinRequest") MemberJoinRequest memberJoinRequest,
                           BindingResult bindingResult) {

        log.info("isPasswordEqualToPasswordConfirm : {}", memberJoinRequest.isPasswordEqualToPasswordConfirm());
        if (memberJoinRequest.isPasswordEqualToPasswordConfirm() == false) {
            bindingResult.addError(new FieldError("memberJoinRequest", "passwordConfirm", "비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {}", bindingResult);

            return "auth/join-main";
        }

        log.info("requset : {}", memberJoinRequest);
        return "redirect:/user/join/success";
    }

    @GetMapping("/join/success")
    public String joinSuccessPage(Model model) {
        model.addAttribute("member", new MemberJoinRequest());
        model.addAttribute("genderTypes", GenderType.values());
        return "auth/join-success";
    }

}
