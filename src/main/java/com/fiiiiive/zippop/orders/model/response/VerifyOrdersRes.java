package com.fiiiiive.zippop.orders.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VerifyOrdersRes {
    private String impUid;
    private Map<String, Double> productIdxMap;
    private Long storeIdx;
    private Integer totalPeople;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
