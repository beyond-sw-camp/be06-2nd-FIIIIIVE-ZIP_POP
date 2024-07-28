package com.fiiiiive.zippop.post.model.response;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.post.model.PostImage;
import lombok.*;
import retrofit2.http.POST;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPostRes {
    private Long postIdx;
    private String customerEmail;
    private String postTitle;
    private String postContent;
    private Integer postLikeCount;
    private List<GetPostImageRes> getPostImageRes;
//    private List<GetCommentRes> getCommentRes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
