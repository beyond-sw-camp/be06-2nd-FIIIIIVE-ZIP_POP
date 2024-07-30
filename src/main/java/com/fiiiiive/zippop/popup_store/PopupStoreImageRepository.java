package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import com.fiiiiive.zippop.post.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PopupStoreImageRepository extends JpaRepository<PopupStoreImage, Long> {
//    @Query("SELECT pi FROM PostImage pi WHERE pi.post.postIdx = :postIdx")
//    Optional<List<PostImage>> findByPostIdx(Long postIdx);
}
