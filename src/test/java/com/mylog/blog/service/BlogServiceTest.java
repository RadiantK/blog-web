package com.mylog.blog.service;

import com.mylog.blog.domain.Blog;
import com.mylog.blog.dto.BlogRequest;
import com.mylog.blog.repository.BlogRepository;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    Member member;
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
        em.clear();
    }

    @DisplayName("블로그 정보 없을 때 조회")
    @Test
    void notExistMemberFindBlogInfoTest() {
        //Given

        //When
        Blog blog = blogService.findBlogInfoUseEmail(email);

        //Then
        assertThat(blog).isNull();
    }

    @DisplayName("블로그 정보 있을 때 조회")
    @Test
    void existMemberFindBlogInfoTest() {
        //Given
        Blog req = Blog.builder()
                .name("내블로그")
                .nickname("닉네임")
                .description(null)
                .member(this.member)
                .build();
        blogRepository.save(req);

        //When
        Blog blog = blogService.findBlogInfoUseEmail(email);

        //Then
        assertThat(blog).isNotNull();
    }

    @DisplayName("블로그 정보 저장")
    @Test
    void blogSaveTest() {
        //Given
        BlogRequest request = new BlogRequest();
        request.setName("내블로그");
        request.setNickname("닉네임");
        request.setDescription(null);
        request.setEmail(email);

        // When
        Blog blog = blogService.saveBlogInfo(request);

        //Then
        Blog findBlog = blogRepository.findByMember(member).get();
        assertThat(blog.getId()).isEqualTo(findBlog.getId());
    }

    @DisplayName("블로그 정보 수정")
    @Test
    void blogEditTest() {
        //Given
        BlogRequest request = new BlogRequest();
        request.setName("내블로그");
        request.setNickname("닉네임");
        request.setDescription(null);
        request.setEmail(email);

        Blog blog = blogService.saveBlogInfo(request);

        BlogRequest editRequest = new BlogRequest();
        editRequest.setName("수정한 블로그");
        editRequest.setNickname("수정한닉네임");
        editRequest.setDescription("수정한 내용");
        editRequest.setEmail(email);

        //When
        blogService.editBlogInfo(editRequest);

        //Then
        Blog findBlog = blogRepository.findByMember(member).get();
        assertThat(findBlog.getName()).isEqualTo(editRequest.getName());
        assertThat(findBlog.getNickname()).isEqualTo(editRequest.getNickname());
        assertThat(findBlog.getDescription()).isEqualTo(editRequest.getDescription());
    }

}