package com.fiiiiive.zippop.popup_review;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PopupReviewRepository extends JpaRepository<PopupReview, Long> {
    // 팝업 스토어 인덱스를 기반으로 스토어의 리뷰를 조회
    @Query("SELECT pr FROM PopupReview pr JOIN FETCH pr.popupStore ps WHERE ps.storeIdx = :storeIdx")
    Optional<Page<PopupReview>> findByStoreIdx(Long storeIdx, Pageable pageable);

    // 고객의 이메일를 기반으로 자신이 쓴 리뷰를 전체 조회
    @Query("SELECT pr FROM PopupReview pr JOIN FETCH pr.customer c WHERE c.email = :customerEmail")
    Optional<Page<PopupReview>> findByCustomerEmail(String customerEmail, Pageable pageable);
}
