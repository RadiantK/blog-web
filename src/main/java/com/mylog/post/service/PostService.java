package com.mylog.post.service;

import com.mylog.member.domain.Member;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.*;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 개인 블로그 메인 게시물 페이징 처리
    public Page<PostDataResponse> blogMainPagePost(PostDataRequest request) {
        Member member = getMember(request.getEmail());

        if (request.getSearch() == null) {
            request.setSearch("");
        }

        return postRepository.findAllMainPage(
                member,
                "%" + request.getSearch() + "%",
                PageRequest.of(request.getPage() - 1, request.getPagingSize()));
    }

    // 마이페이지 게시글 관리 페이지 페이징 처리
    public Page<PostAndCategoryResponse> postAndCategoryPaging(PostDataRequest request) {
        Member member = getMember(request.getEmail());

        if (request.getSearch() == null) {
            request.setSearch("");
        }

        return postRepository.findPostAndCategoryAll(
                member,
                "%" + request.getSearch() + "%",
                PageRequest.of(request.getPage() - 1, request.getPagingSize()));
    }

    public List<PostDataResponse> myPageMainPost() {
        List<Post> post = postRepository.findTop3ByOrderByIdDesc();
        List<PostDataResponse> list = post.stream()
                .map(p -> {
                    return new PostDataResponse(p.getId(), p.getTitle(), p.getContent(), p.getCreatedAt());
                }).collect(Collectors.toList());
        
        return list;
    }

    // 게시물 저장
    public Post savePost(PostSaveRequest request) {
        Member member = getMember(request.getEmail());

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .category(category)
                .build();

        return postRepository.save(post);
    }

    // 게시물 상세
    public PostDetailResponse detailPost(Long num) {
        Post post = postRepository.findPostAndCategory(num).orElse(null);
        return PostDetailResponse.of(post);
    }

    // 게시물 수정
    public void editPost(PostEditRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        post.edit(request.getTitle(), request.getContent(), category);
        log.info("post : {}", post);
    }

    // 게시물 삭제
    public void removePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        postRepository.delete(post);
    }

    // 회원 체크
    private Member getMember(String email) {
        return memberRepository.findByEmailAndEnabled(email, 1)
                .orElseThrow(MemberNotFoundException::new);
    }

}
