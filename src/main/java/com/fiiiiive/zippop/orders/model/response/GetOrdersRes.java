package com.fiiiiive.zippop.orders.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetOrdersRes {
    private String impUid;
    private Long productIdx;
}
