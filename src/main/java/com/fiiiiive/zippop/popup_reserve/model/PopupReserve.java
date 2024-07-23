package com.fiiiiive.zippop.popup_reserve.model;


import com.fiiiiive.zippop.popup_store.model.PopupStore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupReserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reserveIdx;
    private String reserveUUID;
    private String reserveWaitingUUID;
    private Long maxCount;
    private Long expiredTime; // 날짜 형식으로 변환

    @ManyToOne
    @JoinColumn(name ="store_idx")
    private PopupStore popupStore;
}
