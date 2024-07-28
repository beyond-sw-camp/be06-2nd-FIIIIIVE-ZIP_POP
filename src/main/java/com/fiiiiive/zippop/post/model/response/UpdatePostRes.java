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
    private String postTitle;
    private String postContent;
    private List<GetPostImageRes> getPostImageRes;
//    private List<GetCommentRes> getCommentRes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
