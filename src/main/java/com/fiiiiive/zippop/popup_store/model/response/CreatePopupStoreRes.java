package com.fiiiiive.zippop.popup_store.model.response;

import com.fiiiiive.zippop.post.model.response.GetPostImageRes;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupStoreRes {
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
//    private Integer totalPeople;
}
