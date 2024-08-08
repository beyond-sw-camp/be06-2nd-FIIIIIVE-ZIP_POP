package com.fiiiiive.zippop.orders.model.response;

import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCustomerOrdersDetailRes {
    private Long companyOrdersDetailIdx;
    private Integer eachPrice;
    private String trackingNumber;
    private GetPopupGoodsRes getPopupGoodsRes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
