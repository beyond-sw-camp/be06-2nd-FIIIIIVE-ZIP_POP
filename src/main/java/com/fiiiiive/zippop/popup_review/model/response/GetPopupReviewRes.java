package com.fiiiiive.zippop.popup_review.model.response;

import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPopupReviewRes {
    private Long reviewIdx;
    private String customerEmail;
    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
}
