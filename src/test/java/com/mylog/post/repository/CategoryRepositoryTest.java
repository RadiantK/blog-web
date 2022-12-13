package com.mylog.post.repository;

import com.mylog.global.config.AppConfig;
import com.mylog.global.config.JpaConfig;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

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
        Category category2 = new Category("dbdb", member);
        Category category3 = new Category("dbdbdb", member);
        categoryRepository.save(category);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        em.flush();
        em.clear();
    }

    @DisplayName("회원에 해당하는 카테고리 출력")
    @Test
    void findAllByMemberTest() {
        List<Category> categories = categoryRepository.findAllByMember(member);
        categories.forEach(c -> log.info("categories : {}", c));

        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories.isEmpty()).isFalse();
    }

    @DisplayName("카테고리명과 회원에 대한 조회")
    @Test
    void findByNameAndMemberTest() {
        Category category = categoryRepository.findByNameAndMember("db", member).orElse(null);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo("db");
    }
}