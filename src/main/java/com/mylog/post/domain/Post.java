package com.mylog.post.domain;

import com.mylog.global.common.BaseEntity;
import com.mylog.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 제목

    @Lob
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    @Builder
    public Post(String title, String content, Category category, Member member) {
        this.title = title;
        this.content = content;
        this.category = category;
        setMember(member);
    }

    public void edit(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
