package com.fiiiiive.zippop.popup_goods.model.response;

import com.fiiiiive.zippop.popup_goods.model.PopupGoodsImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePopupGoodsRes {
    private Long productIdx;
    private String productName;
    private Integer productPrice;
    private Integer productAmount;
    private String productContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPopupGoodsImageRes> popupGoodsImageList = new ArrayList<>();
}
