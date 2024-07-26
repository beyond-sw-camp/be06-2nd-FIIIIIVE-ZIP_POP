package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCustomerEmail(String email, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
}
