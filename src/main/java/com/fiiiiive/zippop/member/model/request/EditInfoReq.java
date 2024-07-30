package com.fiiiiive.zippop.member.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditInfoReq {
    private String name;
    private String crn;
    private String phoneNumber;
    private String address;
}
