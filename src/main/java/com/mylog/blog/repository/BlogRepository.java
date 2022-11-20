package com.mylog.blog.repository;

import com.mylog.blog.domain.Blog;
import com.mylog.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByMember(Member member);
}
