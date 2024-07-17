package com.fiiiiive.zippop.member;
import com.fiiiiive.zippop.common.baseresponse.BaseResponse;
import com.fiiiiive.zippop.common.baseresponse.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.request.PostSignupReq;
import com.fiiiiive.zippop.member.model.response.PostSignupRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "member-service-api", description = "Member Application")
@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<BaseResponse<PostSignupRes>> signup(@RequestBody PostSignupReq request) throws Exception {
        String uuid = memberService.sendEmail(request.getEmail(), request.getRole());
        PostSignupRes response = memberService.signup(request);
        emailVerifyService.save(request.getEmail(), uuid);
        return ResponseEntity.ok(new BaseResponse(response, BaseResponseMessage.MEMBER_REGISTER_SUCCESS));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    public ResponseEntity<BaseResponse> verify(String email, String role, String uuid) {
        Boolean verifyResponse = emailVerifyService.isExist(email, uuid);
        if (verifyResponse) {
            Boolean activeResponse = memberService.activeMember(email, role);
            return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.MEMBER_EMAIL_VERIFY_SUCCESS));
        } else {
            return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL));
        }
    }
}