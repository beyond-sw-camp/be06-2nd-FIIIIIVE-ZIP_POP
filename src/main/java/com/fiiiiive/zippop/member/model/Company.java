package com.fiiiiive.zippop.member.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.orders.model.CompanyOrders;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyIdx;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false, length = 100, unique = true)
    private String password;
    private String name;
    private String crn; // 사업자 등록 번호
    private String phoneNumber;
    private String address;
    private Boolean enabled;
    private Boolean inactive;
    private String role;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<PopupStore> popupStoreList = new ArrayList<>();
    @OneToMany(mappedBy = "company")
    private List<CompanyOrders> companyOrdersList = new ArrayList<>();
}

