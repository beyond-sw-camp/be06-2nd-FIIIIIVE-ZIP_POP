package com.fiiiiive.zippop.post.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPostRes {
    private String postTitle;
    private String postContent;
    private String postDate;
    private String email;
}
