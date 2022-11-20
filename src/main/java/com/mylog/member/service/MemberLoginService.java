package com.mylog.member.service;

import com.mylog.blog.domain.Blog;
import com.mylog.blog.repository.BlogRepository;
import com.mylog.member.domain.Member;
import com.mylog.member.dto.MemberLoginRequest;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.exception.WrongPasswordException;
import com.mylog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlogRepository blogRepository;

    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmailAndEnabled(request.getEmail(), 1)
                .orElseThrow(MemberNotFoundException::new);

        if (isMatchPassword(request.getPassword(), member.getPassword()) == false) {
            throw new WrongPasswordException();
        }

        Blog blog = blogRepository.findByMember(member).orElse(null);

        return blog != null
                ? new MemberLoginResponse(member.getEmail(), member.getRole(), blog.getName())
                : new MemberLoginResponse(member.getEmail(), member.getRole(), null);
    }

    private boolean isMatchPassword(String rawPwd, String encodePwd) {
        return passwordEncoder.matches(rawPwd, encodePwd);
    }
}
