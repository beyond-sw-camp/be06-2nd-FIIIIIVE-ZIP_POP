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
    // 고객 이메일 과 게시글의 인덱스 번호로 해당 게시글의 자신의 좋아요 조회
    @Query("SELECT pl FROM PostLike pl " +
            "JOIN FETCH pl.customer " +
            "JOIN FETCH pl.post " +
            "WHERE pl.customer.email = :customerEmail " +
            "AND pl.post.postIdx = :postIdx")
    Optional<PostLike> findByCustomerEmailAndPostIdx(String customerEmail, Long postIdx);

    // 고객 인덱스 번호와 게시글의 인덱스 번호로 해당 게시글의 자신의 좋아요 삭제
    @Modifying
    @Query("DELETE FROM PostLike pl " +
            "WHERE pl.customer.email = :customerEmail " +
            "AND pl.post.postIdx = :postIdx")
    void deleteByCustomerEmailAndPostIdx(String customerEmail, Long postIdx);
}