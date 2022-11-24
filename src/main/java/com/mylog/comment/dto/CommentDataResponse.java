package com.mylog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NotBlank
@AllArgsConstructor
public class CommentDataResponse {

    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

}
