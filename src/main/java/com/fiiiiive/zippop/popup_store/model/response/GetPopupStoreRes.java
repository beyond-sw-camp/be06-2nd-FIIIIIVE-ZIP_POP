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
    private String storeName;
    private String storeAddr;
    private String storeDate;
    private String category;
    private List<GetPopupReviewRes> reviews = new ArrayList<>();
    private List<GetPopupGoodsRes> popupGoodsList = new ArrayList<>();
    private String storeContent;
    private Integer companyIdx;
    private Integer rating;
    private String storeImage;
    private Integer totalPeople;
}
