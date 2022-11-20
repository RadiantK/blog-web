package com.mylog.post.dto;

import lombok.Data;

@Data
public class PostEditRequest {

    private Long postId;
    private Long categoryId;
    private String title;
    private String content;
}
