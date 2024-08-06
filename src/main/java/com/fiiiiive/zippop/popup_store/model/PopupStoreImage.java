package com.fiiiiive.zippop.popup_store.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PopupStoreImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeImageIdx;
    @Column(columnDefinition="varchar(255) CHARACTER SET UTF8")
    private String imageUrl;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private PopupStore popupStore;
}
