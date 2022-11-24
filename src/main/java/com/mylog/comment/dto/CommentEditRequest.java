package com.mylog.comment.dto;

import lombok.Data;

@Data
public class CommentEditRequest {

    private Long id;
    private String password;
    private String content;
}
