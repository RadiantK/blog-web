package com.mylog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMainResponse {

    private Long id;

    private String title;

    private String Content;

    private LocalDateTime createdAt;
}
