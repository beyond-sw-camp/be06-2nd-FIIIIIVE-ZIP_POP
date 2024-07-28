package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCustomerEmail(String email, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword% OR p.customerEmail LIKE %:keyword%")
    Page<Post> findByKeyword(Pageable pageable, @Param("keyword") String keyword);
}
