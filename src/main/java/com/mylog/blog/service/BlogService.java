package com.mylog.blog.service;

import com.mylog.blog.domain.Blog;
import com.mylog.blog.dto.BlogRequest;
import com.mylog.blog.repository.BlogRepository;
import com.mylog.member.domain.Member;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;

    public Blog findBlogInfo(String email) {
        Member member = getMember(email);

        return blogRepository.findByMember(member).orElse(null);
    }

    public Blog saveBlogInfo(BlogRequest request) {
        Member member = getMember(request.getEmail());

//        blogRepository.findByNickname(request.getNickname())
//                .orElseThrow(DuplicateNicknameException::new);

        Blog blog = Blog.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .description(request.getDescription())
                .member(member)
                .build();

        return blogRepository.save(blog);
    }

    public void editBlogInfo(BlogRequest request) {
        Blog blog = blogRepository.findByMember(getMember(request.getEmail())).orElse(null);

        blog.editBlog(request.getName(), request.getNickname(), request.getDescription());
    }

    public void deleteBlog(String email) {
        Blog blog = blogRepository.findByMember(getMember(email)).orElse(null);

        blogRepository.delete(blog);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmailAndEnabled(email, 1)
                .orElseThrow(MemberNotFoundException::new);
    }
}
