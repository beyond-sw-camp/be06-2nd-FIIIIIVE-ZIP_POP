package com.fiiiiive.zippop.popup_goods.model.request;

import lombok.*;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class CreatePopupGoodsReq {
    private String productName;
    private Integer productPrice;
    private Integer productAmount;
    private String storeName;
}
