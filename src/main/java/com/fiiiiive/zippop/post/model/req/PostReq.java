package com.fiiiiive.zippop.post.model.req;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReq {
    private String postTitle;
    private String postContent;
    private String postDate;
    private String email;
}
