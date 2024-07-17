package org.fiiiiive.zippop.popup_goods;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.fiiiiive.zippop.popup_store.PopupStore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productIdx;

    private String productName;
    private Integer productPrice;
    private String productContent;
    private String productImg;
    private Integer productAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_idx")
    @JsonBackReference
    private PopupStore popupStore;
    private String storeName; // 추가된 필드
}
