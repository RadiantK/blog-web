package com.mylog.post.dto;

import com.mylog.post.domain.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostEditData {

    private Long postId;
    private String title;
    private String content;
    private List<Category> category;

}
