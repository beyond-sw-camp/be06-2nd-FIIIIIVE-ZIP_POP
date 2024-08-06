package com.fiiiiive.zippop.popup_goods.model;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
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
public class PopupGoodsImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PopupGoodsImageIdx;
    private String imageUrl;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productIdx")
    private PopupGoods popupGoods;
}
