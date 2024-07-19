package com.fiiiiive.zippop.member;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.request.PostSignupReq;
import com.fiiiiive.zippop.member.model.response.PostSignupRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "member-api", description = "Member")
@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<PostSignupRes>> signup(@RequestBody PostSignupReq dto) throws Exception {
        String uuid = memberService.sendEmail(dto);
        PostSignupRes response = memberService.signup(dto);
        emailVerifyService.save(dto, uuid);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.MEMBER_REGISTER_SUCCESS, response));
    }

    @GetMapping("/verify")
    public ResponseEntity<BaseResponse> verify(String email, String role, String uuid) throws Exception {
        emailVerifyService.isExist(email, uuid);
        memberService.activeMember(email, role);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.MEMBER_EMAIL_VERIFY_SUCCESS));
    }
}