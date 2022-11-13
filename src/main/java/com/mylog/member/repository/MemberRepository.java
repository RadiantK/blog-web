package com.mylog.member.repository;

import com.mylog.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndEnabled(String email, Integer enabled);
}
