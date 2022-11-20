package com.mylog.post.repository;

import com.mylog.member.domain.Member;
import com.mylog.post.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c join fetch c.member where c.member = :member")
    List<Category> findAllByMember(@Param("member") Member member);

    Optional<Category> findByNameAndMember(String name, Member member);
}
