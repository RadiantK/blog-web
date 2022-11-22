package com.mylog.blog.controller;

import com.mylog.blog.domain.Blog;
import com.mylog.blog.dto.BlogRequest;
import com.mylog.blog.service.BlogService;
import com.mylog.global.argumentResolver.LoginMember;
import com.mylog.global.common.Constant;
import com.mylog.global.common.Pagination;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.dto.MemberEditInfo;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.member.exception.DuplicatedMemberException;
import com.mylog.member.exception.WrongPasswordException;
import com.mylog.member.service.MemberService;
import com.mylog.post.domain.Category;
import com.mylog.post.dto.PostAndCategoryResponse;
import com.mylog.post.dto.PostDataRequest;
import com.mylog.post.dto.PostDataResponse;
import com.mylog.post.service.CategoryService;
import com.mylog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
@Slf4j
public class MyPageController {

    private final MemberService memberService;
    private final PostService postService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    @ModelAttribute("genders")
    public GenderType[] genderTypes() {
        return GenderType.values();
    }

    @GetMapping
    public String myPage(@LoginMember MemberLoginResponse member,
                         Model model) {
        List<PostDataResponse> posts = postService.myPageMainPost();
        log.info("posts : {}", posts);

        Blog blogInfo = blogService.findBlogInfoUseEmail(member.getEmail());
        model.addAttribute("posts", posts);
        model.addAttribute("blog", blogInfo);
        return "mypage/main";
    }

    @GetMapping("/post")
    public String postManagementPage(@LoginMember MemberLoginResponse member,
                           @RequestParam(required = false) String search,
                           @RequestParam(defaultValue = "1") Integer page,
                           Model model) {

        PostDataRequest req = new PostDataRequest();
        req.setEmail(member.getEmail());
        req.setSearch(search);
        req.setPage(page);
        req.setPagingSize(10);

        Page<PostAndCategoryResponse> posts = postService.postAndCategoryPaging(req);
        Pagination pagination = new Pagination(page, posts.getTotalPages());

        model.addAttribute("posts", posts.toList());
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);

        return "mypage/write";
    }

    @GetMapping("/category")
    public String categoryManagementPage(@LoginMember MemberLoginResponse member) {

        return "mypage/category";
    }

    @GetMapping("/blog")
    public String blogManagementPage(@LoginMember MemberLoginResponse member,
                                     Model model) {
        Blog blogInfo = blogService.findBlogInfoUseEmail(member.getEmail());

        if (blogInfo != null) {
            model.addAttribute("blogRequest", BlogRequest.of(blogInfo));
        } else {
            model.addAttribute("blogRequest", new BlogRequest());
        }

        return "mypage/blog";
    }

    @PostMapping("/blog")
    public String blogManagement(@LoginMember MemberLoginResponse member,
                                 @Valid BlogRequest blogRequest,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "mypage/blog";
        }

        blogRequest.setEmail(member.getEmail());
        log.info("blogRequest : {}", blogRequest);
        Blog blog = blogService.saveBlogInfo(blogRequest);
        
        // 세션 블로그 이름 갱신
        HttpSession session = request.getSession();
        session.setAttribute(Constant.LOGIN_MEMBER,
                new MemberLoginResponse(member.getEmail(),member.getRole(), blog.getName()));

        return "redirect:/user/mypage";
    }

    @GetMapping("/edit")
    public String memberInfoManagementPage(@LoginMember MemberLoginResponse member,
                                           Model model) {

        Member findMember = memberService.findMember(member.getEmail());
        MemberEditInfo memberEditInfo = MemberEditInfo.of(findMember);
        model.addAttribute("memberEditInfo", memberEditInfo);
        return "mypage/edit";
    }
    @PostMapping("/edit")
    public String memberInfoManagement(@LoginMember MemberLoginResponse member,
                                       @Valid MemberEditInfo memberEditInfo,
                                       BindingResult bindingResult) {

        log.info("memberEditInfo : {}", memberEditInfo);
        memberEditInfo.setEmail(member.getEmail());
        try {
            memberService.updateMember(memberEditInfo);
        } catch (WrongPasswordException e) {
            bindingResult.addError(new FieldError("memberEditInfo", "passwordConfirm", "비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "mypage/edit";
        }

        return "redirect:/user/mypage";
    }

    @GetMapping("/withdraw")
    public String memberWithdrawalManagementPage() {

        return "mypage/withdraw";
    }
}

