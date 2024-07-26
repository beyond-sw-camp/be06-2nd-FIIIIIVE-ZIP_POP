package com.fiiiiive.zippop.post.model.response;

import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
}