package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByEmail(String email, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.customer WHERE p.email = :email")
    Page<Post> findByEmailFetchJoin(String email, Pageable pageable);
}
