package com.fiiiiive.zippop.popup_goods.model.response;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPopupGoodsRes {
    private Long productIdx;

    private String productName;
    private Integer productPrice;
    private String productContent;
    private String productImg;
    private Integer productAmount;
    private Long storeIdx;
    private String storeName; // 추가된 필드
}
