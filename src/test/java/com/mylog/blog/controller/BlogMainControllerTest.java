package com.mylog.blog.controller;

import com.mylog.blog.service.BlogService;
import com.mylog.global.common.Constant;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.dto.MemberJoinRequest;
import com.mylog.member.dto.MemberLoginRequest;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.member.service.MemberLoginService;
import com.mylog.member.service.MemberService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@AutoConfigureMockMvc
@SpringBootTest
class BlogMainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private BlogService blogService;

    String email = "test5@test.com";

    @DisplayName("[GET] 개인 블로그 메인 페이지 테스트, 회원 로그인 및 블로그 설정이 되어있을 때")
    @Test
    void blogMainTest() throws Exception {
        memberJoinAndLogin();

        mockMvc.perform(get("/blog/main").session(session)
                .param("search", "1")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/myblog"))
                .andExpect(model().attributeExists("posts"));
    }

    @DisplayName("[GET] 개인 블로그 메인 페이지 테스트, 로그인이 안되어 있을 때")
    @Test
    void blogMainTestNotLogin() throws Exception {

        mockMvc.perform(get("/blog/main").session(session)
                .param("search", "1")
                .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mylog.com/user/login?url="));
    }

    private void memberLogin() {
        if (session.getAttribute(Constant.LOGIN_MEMBER) == null) {
            MemberLoginRequest memberLoginRequest = new MemberLoginRequest();
            memberLoginRequest.setEmail(email);
            memberLoginRequest.setPassword("!asdf1234");

            MemberLoginResponse login = memberLoginService.login(memberLoginRequest);
            session.setAttribute(Constant.LOGIN_MEMBER, login);
        }
    }

    private void memberJoinAndLogin() {
        memberJoin();

        memberLogin();
    }

    @Transactional
    private void memberJoin() {
        Member member = memberService.findMember(email);

        if (member == null) {
            MemberJoinRequest request = new MemberJoinRequest();
            request.setEmail(email);
            request.setPassword("!asdf1234");
            request.setPasswordConfirm("!asdf1234");
            request.setName("김자바");
            request.setPhone("010-1111-2222");
            request.setGender(GenderType.MALE);
            request.setPostcode("1234");
            request.setStreet("서울시 구로구");

            memberService.join(request);
        }
    }
}