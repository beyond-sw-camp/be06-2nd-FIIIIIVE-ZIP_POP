package com.fiiiiive.zippop.popup_store.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.favorite.model.Favorite;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
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
//    private Integer companyIdx;
    private Integer rating;
    private String storeImage;
    private Integer totalPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyIdx")
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "popupStore")
    private List<Favorite> favoriteList;
}
