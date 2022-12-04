package com.mylog.member.controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("회원가입 동의 페이지")
    @Test
    void joinAgreePage() throws Exception {
        mockMvc.perform(get("/user/join/agree"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/join-agree"));
    }

    @DisplayName("회원가입 메인페이지 이동")
    @Test
    void joinMainPage() throws Exception {
        mockMvc.perform(get("/user/join/main"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/join-main"));
    }

    @DisplayName("회원가입 성공")
    @Test
    void joinMain() throws Exception {
        mockMvc.perform(post("/user/join/main")
                .param("email", "aaabc@abc.com")
                .param("password", "!abcd1234")
                .param("passwordConfirm", "!abcd1234")
                .param("name", "김이름")
                .param("gender", "MALE")
                .param("phone", "010-2222-4455")
                .param("postcode", "12345")
                .param("street", "서울시 구로구"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/join/success"));
    }

    @DisplayName("회원가입 성공 페이지 이동")
    @Test
    void joinSuccessPage() throws Exception {
        mockMvc.perform(get("/user/join/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/join-success"));
    }

    @DisplayName("로그인 페이지 이동")
    @Test
    void loginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void login() throws Exception {
        memberJoin();

        mockMvc.perform(post("/user/login")
                .param("email", "test@test.com")
                .param("password", "!asdf1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

//    @Test
//    void logout() throws Exception {
//    }
//
//    @Test
//    void findIdPage() throws Exception {
//    }
//
//    @Test
//    void findPwdPage() throws Exception {
//    }
//
//    @Test
//    void deleteMember() throws Exception {
//    }

    private void memberJoin() {
        Member member = memberService.findMember("test@test.com");

        if (member == null) {
            MemberJoinRequest request = new MemberJoinRequest();
            request.setEmail("test@test.com");
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