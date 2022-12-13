package com.mylog.post.repository;

import com.mylog.global.config.AppConfig;
import com.mylog.global.config.JpaConfig;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.PostDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@Slf4j
@DataJpaTest
@Import({AppConfig.class, JpaConfig.class})
class PostRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    EntityManager em;

    Member member;
    Category category;
    String email = "test@test.com";

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email(email)
                .password("1234")
                .name("김자바")
                .gender(GenderType.MALE)
                .phone("010-1234-4564")
                .address(new Address("1234", "서울시", "22층"))
                .role(RoleType.ROLE_MEMBER)
                .enabled(1)
                .build();

        memberRepository.save(member);

        category = new Category("db", member);
        categoryRepository.save(category);

        for (int i = 1; i <= 7; i++) {
            Post post = Post.builder()
                    .title("제목" + i)
                    .content("내용" + i)
                    .member(member)
                    .category(category)
                    .build();

            postRepository.save(post);
        }

        em.flush();
        em.clear();
    }

    @Test
    void findPostMainPageTest() {
        Member member = memberRepository.findByEmailAndEnabled(email, 1).orElse(null);

        Page<PostDataResponse> posts =
                postRepository.findAllMainPage(member, "%%", PageRequest.of(1, 5));

        log.info("============모든 게시물 출력============");

        log.info("getTotalPages: {}", posts.getTotalPages());
        log.info("getTotalElements: {}", posts.getTotalElements());
        posts.forEach(s -> log.info("post : {}", s));
        assertThat(posts.getTotalPages()).isEqualTo(2);
        assertThat(posts.getTotalElements()).isEqualTo(7);
    }

    @Test
    void findTop3OrderByIdDescTest() {
        List<Post> list = postRepository.findTop3ByOrderByIdDesc();

        List<PostDataResponse> result = new ArrayList<>();
        for (Post post : list) {
            result.add(new PostDataResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt()));
        }

        log.info("result : {}", result);
        assertThat(result.size()).isEqualTo(3);
    }
}