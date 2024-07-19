package com.fiiiiive.zippop.popup_review.model.response;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPopupReviewRes {
    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private String reviewDate;
    private GetPopupStoreRes getPopupStoreRes;
    private String storeName;
}
