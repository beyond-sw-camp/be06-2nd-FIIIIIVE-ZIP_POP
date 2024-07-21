package com.fiiiiive.zippop.member.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostSignupRes {
    private Long idx;
    private Boolean enabled;
    private String role;
    private String email;
}

