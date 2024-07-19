package com.fiiiiive.zippop.popup_goods.model.response;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_store.model.response.PopupStoreRes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupGoodsRes {
    private Long productIdx;

    private String productName;
    private Integer productPrice;
    private String productContent;
    private String productImg;
    private Integer productAmount;
    private PopupStoreRes popupStoreRes;
    private String storeName; // 추가된 필드

    public PopupGoodsRes convertToPopupGoodsRes(PopupGoods popupGoods) {
        PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
        popupGoodsRes.setProductIdx(popupGoods.getProductIdx());
        popupGoodsRes.setProductName(popupGoods.getProductName());
        popupGoodsRes.setProductPrice(popupGoods.getProductPrice());
        popupGoodsRes.setProductContent(popupGoods.getProductContent());
        popupGoodsRes.setProductImg(popupGoods.getProductImg());
        popupGoodsRes.setProductAmount(popupGoods.getProductAmount());
        return popupGoodsRes;
    }

}
