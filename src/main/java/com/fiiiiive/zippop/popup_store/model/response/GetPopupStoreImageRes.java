package com.fiiiiive.zippop.popup_store.model.response;

import com.fiiiiive.zippop.post.model.response.GetPostImageRes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPopupStoreImageRes {
    private Long storeImageIdx;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
