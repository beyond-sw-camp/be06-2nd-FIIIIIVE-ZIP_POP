package com.fiiiiive.zippop.popup_store.model.res;


import com.fiiiiive.zippop.popup_goods.model.res.PopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.res.PopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupStoreRes {
    private String storeName;
    private String storeAddr;
    private String storeDate;
    private String category;
    private List<PopupReviewRes> reviews = new ArrayList<>();
    private List<PopupGoodsRes> popupGoodsList = new ArrayList<>();
    private String storeContent;
    private Integer companyIdx;
    private Integer rating;
    private String storeImage;
    private Integer totalPeople;

    public PopupStoreRes convertToPopupStoreRes(PopupStore popupStore) {
        PopupStoreRes popupStoreRes = PopupStoreRes.builder()
                .storeName(popupStore.getStoreName())
                .storeAddr(popupStore.getStoreAddr())
                .storeDate(popupStore.getStoreDate())
                .category(popupStore.getCategory())
                .build();
        return popupStoreRes;
    }
}
