package com.mylog.comment.repository;

import com.mylog.comment.domain.Comment;
import com.mylog.global.config.AppConfig;
import com.mylog.global.config.JpaConfig;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@Slf4j
@DataJpaTest
@Import({AppConfig.class, JpaConfig.class})
class CommentRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

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

        Post post = postRepository.findById(1L).orElse(null);
        Post post2 = postRepository.findById(2L).orElse(null);
        Comment comment = new Comment("김아무개", "1234", "첫 번째 내용", post);
        Comment comment2 = new Comment("두번째", "1111", "두번째 댓글입니다.", post);
        Comment comment3 = new Comment("두번째", "1111", "두번째 댓글입니다.", post2);

        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        em.flush();
        em.clear();
    }

    @Test
    void test() {
        Post post = postRepository.findById(1L).orElse(null);

        List<Comment> list = commentRepository.findByPostId(post.getId());
        log.info("list : {}", list);

        assertThat(list.size()).isEqualTo(2);
    }

}