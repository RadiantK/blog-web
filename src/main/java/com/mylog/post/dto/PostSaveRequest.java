package com.mylog.post.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PostSaveRequest {

    private String email;

    private Long categoryId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;
}
