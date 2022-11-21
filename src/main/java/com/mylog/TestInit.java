package com.mylog;

import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestInit {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .email("user")
                .password(passwordEncoder.encode("1234"))
                .name("김자바")
                .gender(GenderType.MALE)
                .phone("010-1234-4564")
                .address(new Address("1234", "서울시", "22층"))
                .role(RoleType.ROLE_MEMBER)
                .enabled(1)
                .build();

        memberRepository.save(member);

        Category category = new Category("aop", member);
        Category category2 = new Category("database", member);
        Category category3 = new Category("english", member);
        categoryRepository.save(category);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        for (int i = 1; i <= 7; i++) {
            Post post = Post.builder()
                    .title("제목" + i)
                    .content("내용" + i)
                    .member(member)
                    .category(category)
                    .build();

            postRepository.save(post);
        }
    }
}
