package com.fiiiiive.zippop.member.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// AccessToken을 Authorization Header에 담았을때 사용했음
@Getter
@Builder
@AllArgsConstructor
public class PostSignupRes {
    private Long idx;
    private Boolean enabled;
    private Boolean inactive;
    private String role;
    private String email;
}

