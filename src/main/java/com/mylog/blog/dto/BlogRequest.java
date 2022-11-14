package com.mylog.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BlogRequest {

    @NotEmpty
    private String name; // 블로그명

    @NotBlank
    private String nickname; // 닉네임

    private String description; // 블로그 설명

    @NotNull
    private String email;
}
