package com.mylog.member.service;

import com.mylog.member.domain.Address;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.dto.MemberInfoRequest;
import com.mylog.member.dto.MemberJoinRequest;
import com.mylog.member.exception.DuplicatedMemberException;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findMember(String email) {
        Member member = memberRepository.findByEmailAndEnabled(email, 1).orElse(null);

        return member;
    }

    public Member join(MemberJoinRequest request) {
        Member member = memberRepository.findByEmailAndEnabled(request.getEmail(), 1).orElse(null);
        checkDuplicationMember(member);

        Member newMember = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .address(new Address(request.getPostcode(), request.getStreet(), request.getDetail()))
                .role(RoleType.ROLE_MEMBER)
                .enabled(1)
                .build();

        return memberRepository.save(newMember);
    }

    public void updateMember(MemberInfoRequest request) {
        Member member = memberRepository.findByEmailAndEnabled(request.getEmail(), 1).orElse(null);
        existsByMember(member);

        member.editMember(
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhone(),
                request.getGender());
    }

    public void deleteMember(String email) {
        Member member = memberRepository.findByEmailAndEnabled(email, 1).orElse(null);
        existsByMember(member);

        member.removeMember();
    }

    private void checkDuplicationMember(Member member) {
        if (member != null) {
            throw new DuplicatedMemberException();
        }
    }

    private void existsByMember(Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }
    }
}
