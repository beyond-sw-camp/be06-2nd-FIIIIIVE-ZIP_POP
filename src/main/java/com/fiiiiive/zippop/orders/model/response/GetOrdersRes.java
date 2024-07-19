package com.fiiiiive.zippop.orders.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetOrdersRes {
    private String impUid;
    private Map<String, Double> productIdxMap;
}
