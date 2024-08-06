package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.customer WHERE c.post.postIdx = :postIdx")
    Optional<Page<Comment>> findByPostIdx(Long postIdx, Pageable pageable); // Pageable 추가

    @Query("SELECT c FROM Comment c WHERE c.customer.email = :customerEmail")
    Optional<Page<Comment>> findByCustomerEmail(String customerEmail, Pageable pageable); // Pageable 추가

    Optional<Comment> findByCommentIdx(Long commentIdx);
}
