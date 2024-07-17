package com.fiiiiive.zippop.popup_store;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.popup_goods.PopupGoods;
import com.fiiiiive.zippop.popup_review.PopupReview;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @OneToMany(mappedBy = "popupStore")
    @JsonManagedReference
    private List<PopupReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "popupStore")
    @JsonManagedReference
    private List<PopupGoods> popupGoodsList = new ArrayList<>();
    @Column(unique = true)
    private String storeName;
    private String storeAddr;
    private String storeContent;
    private String storeDate;
    private String category;
    private Integer companyIdx;
    private Integer rating;
    private String storeImage;
    private Integer totalPeople;
}
