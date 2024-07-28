package com.fiiiiive.zippop.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartIdx;
    private Integer cartItemCount;
    private Integer cartItemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerIdx")
    @JsonIgnore
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productIdx")
    @JsonIgnore
    private PopupGoods popupGoods;
}