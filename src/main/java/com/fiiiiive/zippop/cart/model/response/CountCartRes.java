package com.fiiiiive.zippop.cart.model.response;

import com.fiiiiive.zippop.cart.model.Cart;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountCartRes {
    private Cart cart;
}
