package com.fiiiiive.zippop.post.model.response;

import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRes {
    private Long postIdx;
    private String customerEmail;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPostImageRes> getPostImageResList;
}
