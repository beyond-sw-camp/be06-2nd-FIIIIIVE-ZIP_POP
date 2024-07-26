package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.post.model.response.CreatePostRes;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "post-api", description = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreatePostRes createPostReq) throws BaseException {
        CreatePostRes response = postService.register(customUserDetails, createPostReq);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_REGISTER_SUCCESS, response));
    }

//    @ExeTimer
    @GetMapping(value = "/search-customer")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchByCustomer(
        @RequestParam String email, @RequestParam int page, @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchByCustomer(email, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_EMAIL_SUCCESS, response));
    }

//    @ExeTimer
    @GetMapping(value = "/search-all")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchAll(
        @RequestParam int page, @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchAll(page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_ALL_SUCCESS, response));
    }

}
