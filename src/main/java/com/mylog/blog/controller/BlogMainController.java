package com.mylog.blog.controller;

import com.mylog.global.argumentResolver.LoginMember;
import com.mylog.global.common.Pagination;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.post.dto.PostDataRequest;
import com.mylog.post.dto.PostDataResponse;
import com.mylog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
@Slf4j
public class BlogMainController {

    private final PostService postService;

    @GetMapping("/main")
    public String blogMain(@LoginMember MemberLoginResponse member,
                         @RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "1") Integer page,
                         Model model) {

        PostDataRequest req = new PostDataRequest();
        req.setEmail(member.getEmail());
        req.setSearch(search);
        req.setPage(page);
        req.setPagingSize(5);

        Page<PostDataResponse> posts = postService.blogMainPagePost(req);
        Pagination pagination = new Pagination(page, posts.getTotalPages());

        model.addAttribute("posts", posts.toList());
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "blog/myblog";
    }

}
