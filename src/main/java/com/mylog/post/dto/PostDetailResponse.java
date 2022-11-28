package com.mylog.post.dto;

import com.mylog.member.domain.Member;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Category category;
    private Member member;

    public static PostDetailResponse of(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getCategory(),
                post.getMember()
        );
    }

}
