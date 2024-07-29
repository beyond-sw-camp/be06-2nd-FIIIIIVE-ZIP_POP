package com.fiiiiive.zippop.popup_store.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupStoreReq {

    private String storeName;
    private String storeAddr;
    private String storeDate;
    private String category;
    private Integer totalPeople;
//    private Long companyIdx;
}
