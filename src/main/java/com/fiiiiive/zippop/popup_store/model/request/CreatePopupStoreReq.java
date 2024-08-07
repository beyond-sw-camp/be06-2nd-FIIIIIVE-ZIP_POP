package com.fiiiiive.zippop.popup_store.model.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupStoreReq {
    private String storeName;
    private String storeAddress;
    private String storeContent;
    private String category;
    private Integer totalPeople;
    private String storeStartDate;
    private String storeEndDate;
}
