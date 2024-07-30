package com.fiiiiive.zippop.cart.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCartReq {
    private Long productIdx;
    private Integer itemCount;
}
