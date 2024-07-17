package org.fiiiiive.zippop.popup_store.res;


import org.fiiiiive.zippop.popup_goods.res.PopupGoodsRes;
import org.fiiiiive.zippop.popup_review.res.PopupReviewRes;
import org.fiiiiive.zippop.popup_store.PopupStore;
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

    private PopupStoreRes convertToPopupStoreRes(PopupStore popupStore) {
        PopupStoreRes popupStoreRes = PopupStoreRes.builder()
                .storeName(popupStore.getStoreName())
                .storeAddr(popupStore.getStoreAddr())
                .storeDate(popupStore.getStoreDate())
                .category(popupStore.getCategory())
                .build();
        return popupStoreRes;
    }
}
