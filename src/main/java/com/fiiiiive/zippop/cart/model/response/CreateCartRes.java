package com.fiiiiive.zippop.cart.model.response;

import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
