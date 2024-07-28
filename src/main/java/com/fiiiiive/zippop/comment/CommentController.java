package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.request.CreateCommentRes;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "comment-api", description = "Comment")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 조회
    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse<CreateCommentRes>> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long postIdx, @RequestBody CreateCommentReq dto) throws BaseException {
        CreateCommentRes response = commentService.register(customUserDetails, postIdx, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_CREATE_SUCCESS, response));
    }

    // 고객 회원이 작성한 댓글 전체 조회
    @GetMapping("/search-customer")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByCustomer(
        @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int page, @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchByCustomer(page, size, customUserDetails);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_FOUND, comments));
    }

    // 한 게시글의 전체 댓글 페이징 조회
    @GetMapping(value = "/search_post")
    public ResponseEntity<BaseResponse<Page<GetCommentRes>>> searchByPost(
        @RequestParam Long postIdx, @RequestParam int page, @RequestParam int size) throws BaseException {
        Page<GetCommentRes> comments = commentService.searchByPost(page, size, postIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_FOUND, comments));
    }
}
