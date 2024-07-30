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
    Optional<Page<Post>> findByCustomerEmail(String CustomerEmail, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    Optional<Post> findByPostIdx(Long postIdx);

    @Query("SELECT p FROM Post p " +
            "WHERE p.title LIKE %:keyword% " +
            "OR p.content LIKE %:keyword% " +
            "OR p.customerEmail LIKE %:keyword%")
    Optional<Page<Post>> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
