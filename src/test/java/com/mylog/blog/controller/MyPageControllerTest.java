package com.mylog.blog.controller;

import com.mylog.blog.dto.BlogRequest;
import com.mylog.blog.service.BlogService;
import com.mylog.global.common.Constant;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.dto.MemberJoinRequest;
import com.mylog.member.dto.MemberLoginRequest;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.member.service.MemberLoginService;
import com.mylog.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLoginService memberLoginService;

    String email = "test@test.com";

    @AfterEach
    void setup() {
        session.invalidate();
    }

    @DisplayName("마이페이지 이동, 로그인 되어있을 때")
    @Test
    void myPageLoginNotBlogSetting() throws Exception {
        memberJoinAndLogin();

        mockMvc.perform(get("/user/mypage").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/main"));
    }

    @DisplayName("마이페이지 이동, 로그인 안되어있을 때")
    @Test
    void myPageNotLogin() throws Exception {

        mockMvc.perform(get("/user/mypage").session(session))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("글 관리 페이지 이동, 로그인 적용")
    @Test
    void postManagementPageLoginOk() throws Exception {
        memberJoinAndLogin();

        mockMvc.perform(get("/user/mypage/post").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/write"));
    }

    @DisplayName("글 관리 페이지 로그인 안되어있을 때")
    @Test
    void postManagementPageNotLogin() throws Exception {
        mockMvc.perform(get("/user/mypage/post").session(session))
                .andExpect(status().is3xxRedirection()).andDo(print());
    }

//    @Test
//    void categoryManagementPage() throws Exception {
//    }
//
//    @Test
//    void blogManagementPage() throws Exception {
//    }
//
//    @Test
//    void blogManagement() throws Exception {
//    }
//
//    @Test
//    void memberInfoManagementPage() throws Exception {
//    }
//
//    @Test
//    void memberInfoManagement() throws Exception {
//    }
//
//    @Test
//    void memberWithdrawalManagementPage() throws Exception {
//    }

    private void memberJoinAndLogin() {
        memberJoin();
        memberLogin();
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