package com.fiiiiive.zippop.orders.model;


import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.Customer;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CompanyOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyOrdersIdx;
    @Column(nullable = false, length = 100, unique = true)
    private String impUid;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "companyIdx")
    private Company company;
    @OneToMany(mappedBy = "companyOrders")
    private List<CompanyOrdersDetail> companyOrdersDetailList = new ArrayList<>();
}
