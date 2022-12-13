package com.mylog.post.service;

import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.*;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@Slf4j
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    EntityManager em;

    Member member;
    Category category;
    String email = "test4@test.com";

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

    @DisplayName("메인페이지 블로그 게시물 출력, 검색어 없을 때")
    @Test
    void mainPagePost() {
        PostDataRequest request = new PostDataRequest();
        request.setSearch(null);
        request.setEmail(email);
        request.setPage(1);
        request.setPagingSize(5);

        Page<PostDataResponse> responsePage = postService.blogMainPagePost(request);

        assertThat(responsePage.getTotalPages()).isEqualTo(2);
        assertThat(responsePage.getTotalElements()).isEqualTo(7);
    }

    @DisplayName("카테고리 없을 때 게시물 저장")
    @Test
    void savePostNotExistCategory() {
        //Given
        PostSaveRequest request = new PostSaveRequest();
        request.setTitle("저장할 제목");
        request.setContent("저장할 내용");
        request.setCategoryId(null);
        request.setEmail(email);

        //When
        Post savePost = postService.savePost(request);
        log.info("savePost : {}", savePost);

        //Then
        assertThat(savePost).isNotNull();
        assertThat(savePost.getCategory()).isNull();
    }

    @DisplayName("카테고리 있을 때 게시물 저장")
    @Test
    void savePostExistCategory() {
        log.info("====================================");
        log.info("====================================");

        //Given
        PostSaveRequest request = new PostSaveRequest();
        request.setTitle("저장할 제목");
        request.setContent("저장할 내용");
        request.setCategoryId(category.getId());
        request.setEmail(email);

        //When
        Post savePost = postService.savePost(request);
        log.info("savePost : {}", savePost);

        //Then
        assertThat(savePost).isNotNull();
        assertThat(savePost.getCategory()).isNotNull();
    }

    @DisplayName("블로그 게시물 상세정보")
    @Test
    void detailPostExistCategory() {
        //Given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .category(category)
                .build();

        Post save = postRepository.save(post);
        log.info("save : {}", save);
        em.detach(save);

        //When&Then
        PostDetailResponse response = postService.detailPost(save.getId());
        log.info("PostDetailResponse : {}", response);

        assertThat(response.getPostId()).isEqualTo(save.getId());
        assertThat(response.getTitle()).isEqualTo(save.getTitle());
        assertThat(response.getContent()).isEqualTo(save.getContent());
        assertThat(response.getCategory().getId()).isEqualTo(save.getCategory().getId());
    }

    @DisplayName("블로그 게시물 상세정보, 카테고리 X")
    @Test
    void detailPostNotExistCategory() {
        //Given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .category(null)
                .build();

        Post save = postRepository.save(post);
        log.info("save : {}", save);
        em.detach(save);

        //When&Then
        PostDetailResponse response = postService.detailPost(save.getId());
        log.info("PostDetailResponse : {}", response);

        assertThat(response.getPostId()).isEqualTo(save.getId());
        assertThat(response.getTitle()).isEqualTo(save.getTitle());
        assertThat(response.getContent()).isEqualTo(save.getContent());
        assertThat(response.getCategory()).isNull();
    }

    @DisplayName("블로그 게시물 수정")
    @Test
    void editPost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .category(category)
                .build();

        Post save = postRepository.save(post);
        em.flush();
        em.clear();
        log.info("==============================================");

        PostEditRequest req = new PostEditRequest();
        req.setPostId(save.getId());
        req.setCategoryId(category.getId());
        req.setTitle("수정한 제목");
        req.setContent("수정한 내용");
        postService.editPost(req);

        Post findPost = postRepository.findById(req.getPostId()).get();
        em.flush();

        assertThat(findPost.getTitle()).isEqualTo(req.getTitle());
        assertThat(findPost.getContent()).isEqualTo(req.getContent());
    }

    @DisplayName("마이페이지 최근 게시물 3개")
    @Test
    void myPageMainPostTest() {
        //Given

        //When&Then
        List<PostDataResponse> list = postService.myPageMainPost();

        list.forEach(p -> log.info("list : {}", p));
        assertThat(list.size()).isEqualTo(3);
    }

    @DisplayName("게시글 삭제")
    @Test
    void removePostTest() {
        //Given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .category(category)
                .build();

        Post save = postRepository.save(post);

        //When&Then
        postService.removePost(post.getId());

        Post findPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(findPost).isNull();
    }

}