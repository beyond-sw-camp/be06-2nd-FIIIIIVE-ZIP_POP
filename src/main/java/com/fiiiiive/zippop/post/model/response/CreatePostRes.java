package com.fiiiiive.zippop.post.model.response;

import com.fiiiiive.zippop.post.model.PostImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRes {
    private Long postIdx;
    private String customerEmail;
    private String postTitle;
    private String postContent;
    private Integer postLikeCount;
    private List<GetPostImageRes> postImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}