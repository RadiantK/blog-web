package com.mylog.post.repository;

import com.mylog.member.domain.Member;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.PostAndCategoryResponse;
import com.mylog.post.dto.PostDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 제목으로 검색하는 메인 페이지 블로그 페이징 처리
    @Query("select new com.mylog.post.dto.PostDataResponse(p.id, p.title, p.content, p.createdAt) from Post p " +
            "where p.member = :member and p.title like :title order by p.id desc")
    Page<PostDataResponse> findAllMainPage(
            @Param("member") Member member, @Param("title") String title, Pageable pageable);

    // 제목으로 회원에 해당하는 카테고리 및 게시글 페이징 처리
    @Query(value = "select new com.mylog.post.dto.PostAndCategoryResponse(p.id, p.title, p.content, p.createdAt, p.category.name) from Post p left join p.category " +
            "where p.member = :member and p.title like :title order by p.id desc")
    Page<PostAndCategoryResponse> findPostAndCategoryAll(
            @Param("member") Member member, @Param("title") String title, Pageable pageable);

    List<Post> findTop3ByOrderByIdDesc();

    // 블로그 게시물과 카테고리를 outerjoin
    @Query("select p from Post p left join fetch p.category where p.id = :id")
    Optional<Post> findPostAndCategory(@Param("id") Long id);
}
