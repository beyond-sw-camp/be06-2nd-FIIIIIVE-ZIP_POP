package com.fiiiiive.zippop.popup_review.model.response;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_store.model.response.PopupStoreRes;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupReviewRes {
    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private String reviewDate;
    private PopupStoreRes popupStoreRes;
    private String storeName;

    public PopupReviewRes convertToReviewRes(PopupReview review) {
        PopupReviewRes popupReviewRes = new PopupReviewRes();
        popupReviewRes.setReviewTitle(review.getReviewTitle());
        popupReviewRes.setReviewContent(review.getReviewContent());
        popupReviewRes.setRating(review.getRating());
        popupReviewRes.setReviewDate(review.getReviewDate());
        popupReviewRes.setStoreName(review.getStoreName());
        return popupReviewRes;
    }
}
