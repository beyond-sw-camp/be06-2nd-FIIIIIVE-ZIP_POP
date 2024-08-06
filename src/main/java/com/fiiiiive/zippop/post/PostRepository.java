package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 고객 이메일을 기반으로 해당 고객이 작성한 게시글 조회
    Optional<Page<Post>> findByCustomerEmail(String CustomerEmail, Pageable pageable);

    // 게시글의 인덱스 번호로 Post 조회
    Optional<Post> findByPostIdx(Long postIdx);

    // 검색어 기반으로 전체 조회
    @Query("SELECT p FROM Post p " +
            "WHERE p.title LIKE %:keyword% " +
            "OR p.content LIKE %:keyword% " +
            "OR p.customerEmail LIKE %:keyword%")
    Optional<Page<Post>> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 전체 게시글 조회
    Page<Post> findAll(Pageable pageable);
}
