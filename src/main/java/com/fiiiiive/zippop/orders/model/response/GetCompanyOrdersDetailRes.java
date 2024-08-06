package com.fiiiiive.zippop.orders.model.response;

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
    private String totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
