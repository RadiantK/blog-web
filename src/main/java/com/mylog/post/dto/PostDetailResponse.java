package com.mylog.post.dto;

import com.mylog.member.domain.Member;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
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
        // 카테고리가 없을 때의 nullPointerException 방지
//        if (post.getCategory() == null) {
//            return new PostDetailResponse(
//                    post.getId(),
//                    post.getTitle(),
//                    post.getContent(),
//                    post.getCreatedAt(),
//                    null
//            );
//        }
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
