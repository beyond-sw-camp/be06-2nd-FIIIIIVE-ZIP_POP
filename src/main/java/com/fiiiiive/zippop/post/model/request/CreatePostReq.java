package com.fiiiiive.zippop.post.model.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostReq {
    private String title;
    private String content;
}
