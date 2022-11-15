package com.mylog.post.dto;

import lombok.Data;

@Data
public class PostMainRequest {

    private String email;
    private String search;
    private int page;
}
