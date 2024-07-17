package com.fiiiiive.zippop.popup_store.model.req;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupStoreReq {

    private String storeName;
    private String storeAddr;
    private String storeDate;
    private String category;
    private Long companyIdx;
}
