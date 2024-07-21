package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


   // List<Comment> findByPost_Idx(Long postId);
    @Query("SELECT c FROM Comment c JOIN FETCH c.customer WHERE c.post.idx = :postId")
    List<Comment> findByPost_Idx(Long postId);
}
