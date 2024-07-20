package com.fiiiiive.zippop.cart.model.response;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCartRes {
    private Long cartIdx;
    private Integer count;
    private Integer price;
    private PopupGoods popupGoods;
}
