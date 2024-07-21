package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "comment-api", description = "Comment")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse<Void>> createComment(@RequestParam Long postId, @RequestBody CreateCommentReq createCommentReq) {
        try {
            commentService.createComment(postId, createCommentReq);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_CREATE_SUCCESS));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.COMMENT_CREATE_FAIL));
        }
    }

    @GetMapping("/search_by_customer")
    public ResponseEntity<BaseResponse<List<GetCommentRes>>> getCommentsByCustomerEmail(@RequestParam String email) {
        try {
            List<GetCommentRes> comments = commentService.getCommentsByCustomerEmail(email);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_FOUND, comments));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.COMMENT_NOT_FOUND));
        }
    }

    @GetMapping(value = "/search_by_post")
    public ResponseEntity<BaseResponse<List<GetCommentRes>>> getCommentsByPost(@RequestParam Long postId) {
        try {
            List<GetCommentRes> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_FOUND, comments));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.COMMENT_NOT_FOUND));
        }
    }
}