package com.fiiiiive.zippop.popup_review;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PopupReviewRepository extends JpaRepository<PopupReview, Long> {

    Page<PopupReview> findByStoreName(String storeName, Pageable pageable);

    @Query("SELECT pr FROM PopupReview pr JOIN FETCH pr.popupStore ps WHERE ps.storeName = :storeName")
    Page<PopupReview> findByStoreNameFetchJoin(String storeName, Pageable pageable);
}
