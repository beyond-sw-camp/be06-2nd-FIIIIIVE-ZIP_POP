package com.fiiiiive.zippop.post.model.response;

import com.fiiiiive.zippop.post.model.Post;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRes {
    private String postTitle;
    private String postContent;
    private String postDate;
    private String email;

    public PostRes convertToPostRes(Post post){
        PostRes postRes = PostRes.builder()
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postDate(post.getPostDate())
                .build();
        return postRes;
    }
}
