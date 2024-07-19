package com.fiiiiive.zippop.popup_review;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PopupReviewRepository extends JpaRepository<PopupReview, Long> {

    Optional<List<PopupReview>> findByStoreName(String storeName);

    @Query("SELECT pr FROM PopupReview pr JOIN FETCH pr.popupStore ps WHERE ps.storeName = :storeName")
    Optional<List<PopupReview>> findByStoreNameWithStore(String storeName);
}
