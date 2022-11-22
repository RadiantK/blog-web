package com.mylog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NotBlank
@AllArgsConstructor
public class PostAndCategoryResponse {

    private Long id;

    private String title;

    private String Content;

    private LocalDateTime createdAt;

    private String name; // 카테고리 이름
}
