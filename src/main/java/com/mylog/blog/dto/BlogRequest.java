package com.mylog.blog.dto;

import com.mylog.blog.domain.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {

    @NotEmpty
    private String name; // 블로그명

    @NotBlank
    private String nickname; // 닉네임

    private String description; // 블로그 설명

    private String email;

    public static BlogRequest of(Blog blog) {
        return new BlogRequest(
                blog.getName(),
                blog.getNickname(),
                blog.getDescription(),
                null);
    }
}
