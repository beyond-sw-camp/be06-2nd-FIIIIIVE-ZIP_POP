package com.fiiiiive.zippop.popup_review.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupReviewRes {
    private Long reviewIdx;
    private String customerEmail;
    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
}
