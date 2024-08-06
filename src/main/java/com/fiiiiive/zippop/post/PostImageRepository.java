package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    // 게시글의 인덱스 번호로 게시글 이미지들 조회
    @Query("SELECT pi FROM PostImage pi " +
            "WHERE pi.post.postIdx = :postIdx")
    Optional<List<PostImage>> findByPostIdx(Long postIdx);
}
