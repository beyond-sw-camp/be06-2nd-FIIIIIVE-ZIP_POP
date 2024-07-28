package com.fiiiiive.zippop.comment.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRes {
    private Long commentIdx;
    private String commentContent;
    private String customerEmail;
    private String createdAt;
    private String updatedAt;
}
