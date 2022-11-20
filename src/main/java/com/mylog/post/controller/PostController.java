package com.mylog.post.controller;

import com.mylog.global.argumentResolver.LoginMember;
import com.mylog.global.common.Pagination;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.*;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
@Slf4j
public class PostController {

    private final PostService postService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/register")
    public String postRegisterPage(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("postSaveRequest", new PostSaveRequest());

        return "blog/regist";
    }

    @PostMapping("/register")
    public String postRegister(@LoginMember MemberLoginResponse member,
                               @Valid PostSaveRequest postSaveRequest,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/blog/regist";
        }

        log.info("postSaveRequest : {}", postSaveRequest);
        postSaveRequest.setEmail(member.getEmail());
        Post post = postService.savePost(postSaveRequest);

        return "redirect:/blog/main";
    }

    @GetMapping("/{postId}")
    public String postDetailPage(@PathVariable Long postId,
                                 Model model) {
        PostDetailResponse postDetailResponse = postService.detailPost(postId);

        model.addAttribute("post", postDetailResponse);
        return "blog/detail";
    }

    @GetMapping("/{postId}/edit")
    public String editPostPage(@PathVariable Long postId,
                               Model model) {
        PostDetailResponse postResponse = postService.detailPost(postId);

        PostEditData postData = PostEditData.builder()
                .postId(postResponse.getPostId())
                .title(postResponse.getTitle())
                .content(postResponse.getContent())
                .category(categoryRepository.findAll())
                .build();

        model.addAttribute("post", postData);

        return "blog/edit";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@PathVariable Long postId,
                           PostEditRequest request) {
        postService.editPost(request);

        return "redirect:/blog/{postId}";
    }

    @GetMapping("/{postId}/remove")
    public String removePost(@PathVariable Long postId) {
        postService.removePost(postId);

        return "redirect:/blog/main";
    }
}
