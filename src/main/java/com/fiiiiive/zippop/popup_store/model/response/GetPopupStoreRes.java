package com.fiiiiive.zippop.popup_store.model.response;


import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPopupStoreRes {
    private Long storeIdx;
    private String storeName;
    private String storeAddress;
    private String storeDate;
    private String category;
    private String storeContent;
    private String companyEmail;
    private Integer likeCount;
    private List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
    private List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
    private List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
}
