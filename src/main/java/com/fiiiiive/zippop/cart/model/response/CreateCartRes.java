package com.fiiiiive.zippop.cart.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCartRes {
    private Long cartIdx;
    private Long customerIdx;
    private Long productIdx;
    private Integer count;
    private Integer price;
}
