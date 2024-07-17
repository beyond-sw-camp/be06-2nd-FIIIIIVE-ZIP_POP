package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByEmail(String email);
}
