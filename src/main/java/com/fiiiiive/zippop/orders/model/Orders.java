package com.fiiiiive.zippop.orders.model;

import com.fiiiiive.zippop.member.model.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String impUid;


    @ManyToOne
    @JoinColumn(name = "Customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "orders")
    private List<OrdersDetail> ordersDetails = new ArrayList<>();

    @Column(updatable = false, nullable = false)
    private Date createdAt;

    private Date updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }


}