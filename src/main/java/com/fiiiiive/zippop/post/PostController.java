package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.CreatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import java.util.List;

@Tag(name = "post-api", description = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse<Void>> register(@RequestBody CreatePostReq createPostReq) {
        try {
            postService.register(createPostReq);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POST_REGISTER_SUCCESS));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.POST_REGISTER_FAIL));

        }
    }

    @GetMapping(value = "/search-customer-email")
    public ResponseEntity<BaseResponse<List<GetPostRes>>> searchCustomerEmail(@RequestParam String email) {
        try {
            List<GetPostRes> postRes = postService.findByCustomerEmail(email);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POST_SEARCH_SUCCESS, postRes));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getCode()).body(new BaseResponse<>(BaseResponseMessage.POST_SEARCH_FAIL));
        }
    }
    @GetMapping(value = "/paged")
    public ResponseEntity<Page<Post>> getAllPostsPaged(@RequestParam int page, @RequestParam int size) throws BaseException {
        Page<Post> postPage = postService.getAllPostsPaged(page, size);
        return ResponseEntity.ok(postPage);
    }
}
