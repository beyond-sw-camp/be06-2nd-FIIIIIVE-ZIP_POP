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
public class GetCompanyOrdersRes {
    private Long companyOrdersIdx;
    private String impUid;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<GetCompanyOrdersDetailRes> getCompanyOrdersDetailResList = new ArrayList<>();
}
