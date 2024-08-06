package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.request.UpdateCommentReq;
import com.fiiiiive.zippop.comment.model.response.CreateCommentRes;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.comment.model.response.UpdateCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "comment-api", description = "Comment")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<CreateCommentRes>> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx,
        @RequestBody CreateCommentReq dto) throws BaseException {
        CreateCommentRes response = commentService.register(customUserDetails, postIdx, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_REGISTER_SUCCESS, response));
    }

    @GetMapping("/search-all")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByPost(
        @RequestParam Long postIdx,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchAll(page, size, postIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_SEARCH_ALL_SUCCESS, comments));
    }

    @GetMapping("/search-customer")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByCustomer(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam int page,
            @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchCustomer(page, size, customUserDetails);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_SEARCH_BY_CUSTOMER_SUCCESS, comments));
    }

    @PatchMapping("/update")
    public ResponseEntity<BaseResponse> update(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx,
        @RequestBody UpdateCommentReq dto) throws BaseException {
        UpdateCommentRes response = commentService.update(customUserDetails, commentIdx, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.COMMENT_UPDATE_SUCCESS,response));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx) throws BaseException {
        commentService.delete(customUserDetails, commentIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.COMMENT_DELETE_SUCCESS));
    }

    @GetMapping("/like")
    public ResponseEntity<BaseResponse> like(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx) throws BaseException {
        commentService.like(customUserDetails, commentIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_LIKE_SUCCESS));
    }

}
