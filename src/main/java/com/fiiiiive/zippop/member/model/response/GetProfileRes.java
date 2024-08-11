package com.fiiiiive.zippop.member.model.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class GetProfileRes {
    private String name;
    private String email;
    private Integer point;
    private String phoneNumber;
    private String address;
    private String crn;
}
