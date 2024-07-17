package com.fiiiiive.zippop.member.model.request;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLoginReq {
    private String role;
    private String email;
    private String password;
    private String crn; // 사업자 등록 번호(Company Registration Number
}
