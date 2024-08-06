// GetPointsRes.java
package com.fiiiiive.zippop.member.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPointRes {
    private Long customer_idx;
    private Integer point;
}
