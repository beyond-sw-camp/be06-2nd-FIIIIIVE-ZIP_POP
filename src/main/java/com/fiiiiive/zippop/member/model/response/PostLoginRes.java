package com.fiiiiive.zippop.member.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLoginRes {
    private String accessToken;
}