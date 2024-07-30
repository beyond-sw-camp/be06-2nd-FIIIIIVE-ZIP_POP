package com.fiiiiive.zippop.cart.model.response;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCartRes {
    private Long cartIdx;
    private Integer itemCount;
    private Integer itemPrice;
    private GetPopupGoodsRes getPopupGoodsRes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
