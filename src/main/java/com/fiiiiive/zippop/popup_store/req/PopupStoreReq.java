package org.fiiiiive.zippop.popup_store.req;

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
    private Integer companyIdx;
}
