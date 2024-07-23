package com.fiiiiive.zippop.popup_reserve.model.response;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupReserveRes {
    private Long reserveIdx;
    private String reserveUUID;
    private String reserveWaitingUUID;
    private Long storeIdx;
}
