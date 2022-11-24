package com.mylog.comment.domain;

import com.mylog.global.common.BaseEntity;
import com.mylog.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private String password;

    @Column(length = 1500)
    private String content;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(String writer, String password, String content, Post post) {
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.post = post;
    }

    public void ediComment(String content) {
        this.content = content;
    }
}
