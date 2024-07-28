package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.PostImage;
import com.fiiiiive.zippop.post.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("SELECT pl FROM PostLike pl " +
            "JOIN FETCH pl.customer " +
            "JOIN FETCH pl.post " +
            "WHERE pl.customer.customerIdx = :customerIdx and pl.post.postIdx = :postIdx")
    Optional<PostLike> findByCustomerIdxAndPostIdx(Long customerIdx, Long postIdx);

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.customer.customerIdx = :customerIdx AND pl.post.postIdx = :postIdx")
    void deleteByCustomerIdxAndPostIdx(Long customerIdx, Long postIdx);
}