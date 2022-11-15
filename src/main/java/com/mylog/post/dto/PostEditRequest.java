package com.mylog.post.dto;

import lombok.Data;

@Data
public class PostEditRequest {

    private Long postId;
    private String categoryName;
    private String title;
    private String content;
}
