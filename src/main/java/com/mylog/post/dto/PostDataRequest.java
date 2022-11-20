package com.mylog.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostDataRequest {

    private String email;
    private String search;
    private int page;
    private int pagingSize;
}
