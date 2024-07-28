package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi FROM PostImage pi WHERE pi.post.postIdx = :postIdx")
    Optional<List<PostImage>> findByPostIdx(Long postIdx);
}
