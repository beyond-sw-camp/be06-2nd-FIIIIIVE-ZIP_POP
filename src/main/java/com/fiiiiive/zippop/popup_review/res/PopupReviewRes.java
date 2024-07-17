package org.fiiiiive.zippop.popup_review.res;

import org.fiiiiive.zippop.popup_review.PopupReview;
import org.fiiiiive.zippop.popup_store.res.PopupStoreRes;
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
