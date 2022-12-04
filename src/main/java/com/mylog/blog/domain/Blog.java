package com.mylog.blog.domain;

import com.mylog.global.common.BaseEntity;
import com.mylog.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 블로그명

    private String nickname; // 닉네임

    private String description; // 블로그 설명

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Blog(String name, String nickname, String description, Member member) {
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.member = member;
    }

    public void editBlog(String name, String nickname, String description) {
        this.name = name;
        this.nickname = nickname;
        this.description = description;
    }
}
