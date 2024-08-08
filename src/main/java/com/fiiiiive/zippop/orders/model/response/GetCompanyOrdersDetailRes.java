package com.fiiiiive.zippop.orders.model.response;

import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCompanyOrdersDetailRes {
    private Long companyOrdersDetailIdx;
    private Integer totalPrice;
    private GetPopupStoreRes getPopupStoreRes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
