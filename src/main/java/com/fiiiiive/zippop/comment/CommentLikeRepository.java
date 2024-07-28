package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT cl FROM CommentLike cl " +
            "JOIN FETCH cl.customer " +
            "JOIN FETCH cl.comment " +
            "WHERE cl.customer.customerIdx = :customerIdx and cl.comment.commentIdx = :commentIdx")
    Optional<CommentLike> findByCustomerIdxAndCommentIdx(Long customerIdx, Long commentIdx);

    @Modifying
    @Query("DELETE FROM CommentLike cl WHERE cl.customer.customerIdx = :customerIdx AND cl.comment.commentIdx = :commentIdx")
    void deleteByCustomerIdxAndCommentIdx(Long customerIdx, Long commentIdx);
}