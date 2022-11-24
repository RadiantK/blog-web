package com.mylog.comment.controller.api;

import com.mylog.comment.dto.CommentDataResponse;
import com.mylog.comment.dto.CommentEditRequest;
import com.mylog.comment.dto.CommentRemoveRequest;
import com.mylog.comment.dto.CommentSaveRequest;
import com.mylog.comment.service.CommentService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class ApiCommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public List<CommentDataResponse> allComment(@PathVariable Long postId) {

        return commentService.AllCommentsByPostId(postId);
    }

    @PostMapping("/add")
    public CommentResult saveComment(@Valid @RequestBody CommentSaveRequest request,
                                     BindingResult bindingResult) {
        commentService.saveComment(request);

        log.info("request : {}", request);
        if (bindingResult.hasErrors()) {
            return CommentResult.builder().result("댓글을 정확히 입력해주세요.").build();
        }

        return CommentResult.builder().result("success").build();
    }

    @PutMapping
    public CommentResult editComment(@Valid @RequestBody CommentEditRequest request,
                                     BindingResult bindingResult) {
        commentService.editComment(request);

        if (bindingResult.hasErrors()) {
            return CommentResult.builder().result("댓글을 입력해주세요.").build();
        }

        return CommentResult.builder().result("success").build();
    }

    @PostMapping("/remove")
    public CommentResult removeComment(@RequestBody CommentRemoveRequest removeRequest) {
        commentService.removeComment(removeRequest);

        return CommentResult.builder().result("success").build();
    }

    @Data
    @Builder
    static class CommentResult {

        private String result;
    }
}
