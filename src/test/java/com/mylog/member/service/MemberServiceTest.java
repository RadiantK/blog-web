package com.mylog.member.service;

import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.dto.MemberInfoRequest;
import com.mylog.member.dto.MemberJoinRequest;
import com.mylog.member.exception.DuplicatedMemberException;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    MemberJoinRequest memberJoinRequest;

    @BeforeEach
    void setup() {
        memberJoinRequest = new MemberJoinRequest();
        memberJoinRequest.setEmail("test@test.com");
        memberJoinRequest.setPassword("asdf1234");
        memberJoinRequest.setName("김자바");
        memberJoinRequest.setPhone("01011112222");
        memberJoinRequest.setGender(GenderType.MALE);
        memberJoinRequest.setPostcode("1234");
        memberJoinRequest.setStreet("서울시 구로구");
    }

    @DisplayName("단일 회원 조회 테스트")
    @Test
    void findMemberTest() {
        //Given
        Member saveMember = memberService.saveMember(memberJoinRequest);

        //When
        Member result = memberService.findMember(saveMember.getEmail());

        //Then
        assertThat(result).isNotNull();
    }

    @DisplayName("단일 회원 조회 실패 테스트")
    @Test
    void findMemberTestFail() {
        //Given

        //When&Then
        assertThatThrownBy(() -> memberService.findMember("test"))
                .isInstanceOf(MemberNotFoundException.class);

    }

    @DisplayName("회원 등록")
    @Test
    void saveMemberTest() {
        //Given
        MemberJoinRequest request = new MemberJoinRequest();
        request.setEmail("test@test.com");
        request.setPassword("asdf1234");
        request.setName("김자바");
        request.setPhone("01011112222");
        request.setGender(GenderType.MALE);
        request.setPostcode("1234");
        request.setStreet("서울시 구로구");

        //When
        Member member = memberService.saveMember(request);
        System.out.println("member = " + member);

        //Then
        Member member1 = memberRepository.findByEmailAndEnabled(member.getEmail(), 1).get();
        assertThat(member.getId()).isEqualTo(member1.getId());
    }

    @DisplayName("중복 회원 등록")
    @Test
    void dupleSaveMemberTest() {
        //Given
        MemberJoinRequest dupleMember = new MemberJoinRequest();
        dupleMember.setEmail("test@test.com");
        dupleMember.setPassword("asdf1234");
        dupleMember.setName("중복회원");
        dupleMember.setPhone("01011112222");
        dupleMember.setGender(GenderType.MALE);
        dupleMember.setPostcode("1234");
        dupleMember.setStreet("서울시 구로구");

        //When
        Member member = memberService.saveMember(memberJoinRequest);
        System.out.println("member = " + member);

        //Then
        assertThatThrownBy(() -> memberService.saveMember(dupleMember))
                .isInstanceOf(DuplicatedMemberException.class);
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMemberTest() {
        //Given
        Member member = memberService.saveMember(memberJoinRequest);
        em.flush();

        MemberInfoRequest request = new MemberInfoRequest();
        request.setEmail(member.getEmail());
        request.setPassword(member.getPassword());
        request.setName("가나다");
        request.setGender(GenderType.FEMALE);
        request.setPhone("010");

        //When
        memberService.updateMember(request);
        em.flush();

        Member result = memberRepository.findByEmailAndEnabled(member.getEmail(), 1).get();

        //Then
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getGender()).isEqualTo(request.getGender());
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getPhone()).isEqualTo(request.getPhone());
    }

    @Test
    void deleteMemberTest() {
        //Given
        Member member = memberService.saveMember(memberJoinRequest);
        em.flush();

        //When & Then
        memberService.deleteMember(member.getEmail());

        Member result = memberRepository.findById(member.getId()).get();

        assertThat(result.getEnabled()).isEqualTo(0);
        assertThat(result.getEnabled()).isEqualTo(member.getEnabled());
    }
}