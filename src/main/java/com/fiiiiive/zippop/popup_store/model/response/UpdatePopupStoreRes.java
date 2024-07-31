package com.fiiiiive.zippop.popup_store.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePopupStoreRes {
    private Long storeIdx;
    private String companyEmail;
    private String storeName;
    private String storeAddress;
    private String storeContent;
    private String storeEndDate;
    private String category;
    private Integer likeCount;
    //    private Integer totalPeople; // => popup_reserve로
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPopupStoreImageRes> getPopupStoreImageResList;
}
