package com.mylog.post.dto;

import lombok.Data;

@Data
public class PostSaveRequest {

    private String email;

    private Long categoryId;

    private String title;

    private String content;
}
