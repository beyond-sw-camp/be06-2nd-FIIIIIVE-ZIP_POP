package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse<Void>> createComment(@PathVariable Long postId, @RequestBody CreateCommentReq createCommentReq) {
        try {
            commentService.createComment(postId, createCommentReq);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_CREATE_SUCCESS));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.COMMENT_CREATE_FAIL));
        }
    }


    @GetMapping(value = "/search")
    public ResponseEntity<BaseResponse<List<GetCommentRes>>> getCommentsByPost(@PathVariable Long postId) {
        try {
            List<GetCommentRes> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.COMMENT_FOUND, comments));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.COMMENT_NOT_FOUND));
        }
    }
}