package com.mylog.member.repository;

import com.mylog.global.config.AppConfig;
import com.mylog.global.config.JpaConfig;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
@Slf4j
@DataJpaTest
@Import({AppConfig.class, JpaConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EntityManager em;

    @Test
    void findMemberTest() {
        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("김자바")
                .phone("010")
                .gender(GenderType.MALE)
                .role(RoleType.ROLE_MEMBER)
                .address(new Address("01112", "서울시 구로구", "11층"))
                .build();

        Member saveMember = memberRepository.save(member);
        em.clear();
        log.info("========================================");
        log.info("========================================");
        log.info("========================================");
        memberRepository.findByEmailAndEnabled("test@test.com", 1);
    }

    @Test
    void saveTest() {
        //Given
        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("김자바")
                .phone("010")
                .gender(GenderType.MALE)
                .role(RoleType.ROLE_MEMBER)
                .address(new Address("01112", "서울시 구로구", "11층"))
                .build();

        //When
        Member result = memberRepository.save(member);

        log.info("result : {}", result);

        //Then
        assertThat(result).isNotNull();
    }
    
    @Test
    void updateTest() {
        //Given
        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("김자바")
                .phone("010")
                .gender(GenderType.MALE)
                .role(RoleType.ROLE_MEMBER)
                .address(new Address("01112", "서울시 구로구", "11층"))
                .build();

        Member saveMember = memberRepository.save(member);
        Long memberId = saveMember.getId();

        //When
        saveMember.editMember(passwordEncoder.encode("1111"), "김수정", "011", GenderType.FEMALE);
        em.flush();

        //Then
        Member findMember = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        assertThat(findMember.getName()).isEqualTo(saveMember.getName());
        assertThat(findMember.getPhone()).isEqualTo(saveMember.getPhone());
        assertThat(findMember.getGender()).isEqualTo(saveMember.getGender());
    }

    @Test
    void deleteTest() {
        //Given
        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("김자바")
                .phone("010")
                .gender(GenderType.MALE)
                .role(RoleType.ROLE_MEMBER)
                .address(new Address("01112", "서울시 구로구", "11층"))
                .build();

        memberRepository.save(member);

        //When
        memberRepository.delete(member);
        em.flush();

        //Then
        assertThatThrownBy(() -> memberRepository.findById(1L).get())
                .isInstanceOf(NoSuchElementException.class);
    }

}