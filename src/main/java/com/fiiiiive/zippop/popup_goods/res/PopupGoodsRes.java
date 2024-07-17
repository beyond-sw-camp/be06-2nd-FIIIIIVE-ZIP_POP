package com.fiiiiive.zippop.popup_goods.res;


import com.fiiiiive.zippop.popup_review.PopupReview;
import com.fiiiiive.zippop.popup_review.res.PopupReviewRes;
import com.fiiiiive.zippop.popup_store.res.PopupStoreRes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupGoodsRes {
    private Long productIdx;

    private String productName;
    private Integer productPrice;
    private String productContent;
    private String productImg;
    private Integer productAmount;
    private PopupStoreRes popupStoreRes;
    private String storeName; // 추가된 필드

    private PopupReviewRes convertToReviewRes(PopupReview review) {
        PopupReviewRes popupReviewRes = new PopupReviewRes();
        popupReviewRes.setReviewTitle(review.getReviewTitle());
        popupReviewRes.setReviewContent(review.getReviewContent());
        popupReviewRes.setRating(review.getRating());
        popupReviewRes.setReviewDate(review.getReviewDate());
        popupReviewRes.setStoreName(review.getStoreName());
        return popupReviewRes;
    }
}
