package com.fiiiiive.zippop.comment.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRes {
    private Long commentIdx;
    private String commentContent;
    private String customerEmail;
    private Integer commentLikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
