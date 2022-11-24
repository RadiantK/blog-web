package com.mylog.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentSaveRequest {

    @NotNull
    private Long postId;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    @NotEmpty
    private String content;
}

