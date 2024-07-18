package com.fiiiiive.zippop.popup_review.req;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupReviewReq {

    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private String reviewDate;
    private String storeName;
}
