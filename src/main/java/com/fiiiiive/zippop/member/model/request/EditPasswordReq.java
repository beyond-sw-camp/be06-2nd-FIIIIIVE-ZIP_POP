package com.fiiiiive.zippop.member.model.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditPasswordReq {
    private String originPassword;
    private String newPassword;
}
