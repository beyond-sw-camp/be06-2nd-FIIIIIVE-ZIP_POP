package com.fiiiiive.zippop.popup_review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PopupReviewRepository extends JpaRepository<PopupReview, Long> {

    @Query("SELECT pr FROM PopupReview pr JOIN pr.popupStore ps WHERE ps.storeName = :storeName")
    public List<PopupReview> findByStoreName(@Param("storeName") String storeName);
}
