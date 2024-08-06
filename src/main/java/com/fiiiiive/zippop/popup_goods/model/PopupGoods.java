package com.fiiiiive.zippop.popup_goods.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fiiiiive.zippop.cart.model.Cart;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_review.model.PopupReviewImage;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PopupGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productIdx;
    private String productName;
    private Integer productPrice;
    private String productContent;
    private Integer productAmount;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "popupGoods")
    private List<Cart> carts;
    @OneToMany(mappedBy = "popupGoods", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PopupGoodsImage> popupGoodsImageList = new ArrayList<>();;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeIdx")
    @JsonBackReference
    private PopupStore popupStore;
}
