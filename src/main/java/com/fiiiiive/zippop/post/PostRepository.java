package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByEmail(String email);

    @Query("SELECT p FROM Post p JOIN FETCH p.customer WHERE p.email = :email")
    Optional<List<Post>> findByEmailFetchJoin(String email);

    @Query("SELECT p FROM Post p")
    Page<Post> findPageBy(Pageable pageable);}
