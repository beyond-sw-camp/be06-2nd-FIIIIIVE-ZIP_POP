package com.fiiiiive.zippop.comment.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCommentRes {
    private Long commentIdx;
    private String commentContent;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}