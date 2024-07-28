package com.fiiiiive.zippop.cart.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCartRes {
    private Long cartIdx;
    private Integer cartItemCount;
    private Integer cartItemPrice;
    private Long customerIdx;
    private Long productIdx;
}
