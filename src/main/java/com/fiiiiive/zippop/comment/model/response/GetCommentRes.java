package com.fiiiiive.zippop.comment.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCommentRes {
    private Long commentId;
    private String content;
    private String email;
    private String createdDate;
}
