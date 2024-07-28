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

    // 댓글 조회
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<CreateCommentRes>> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx,
        @RequestBody CreateCommentReq dto) throws BaseException {
        CreateCommentRes response = commentService.register(customUserDetails, postIdx, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_REGISTER_SUCCESS, response));
    }

    // 고객 회원이 작성한 댓글 전체 조회
    @GetMapping("/search-customer")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByCustomer(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchCustomer(page, size, customUserDetails);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_SEARCH_BY_CUSTOMER_SUCCESS, comments));
    }

    // 한 게시글의 전체 댓글 페이징 조회
    @GetMapping("/search-all")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByPost(
        @RequestParam Long postIdx,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchAll(page, size, postIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_SEARCH_ALL_SUCCESS, comments));
    }

    // 댓글 수정
    @PatchMapping("/update")
    public ResponseEntity<BaseResponse> update(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx,
        @RequestBody UpdateCommentReq dto) throws BaseException {
        UpdateCommentRes response = commentService.update(customUserDetails, commentIdx, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.COMMENT_UPDATE_SUCCESS,response));
    }

    // 댓글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx) throws BaseException {
        commentService.delete(customUserDetails, commentIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.COMMENT_DELETE_SUCCESS));
    }
    // 게시글 추천: commentLikeCount에 대한 동시성 제어는 자원소모에 비해 중요도가 떨어지므로 고려하지 않는다.
    @GetMapping("/like")
    public ResponseEntity<BaseResponse> like(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long commentIdx) throws BaseException {
        commentService.like(customUserDetails, commentIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_LIKE_SUCCESS));
    }

}
