package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.CreatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> register(@RequestBody CreatePostReq createPostReq) throws Exception {
        postService.register(createPostReq);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_REGISTER_SUCCESS));
    }

    @GetMapping(value = "/search-customer-email")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchCustomerEmail(@RequestParam String email,
                                                                              @RequestParam int page,
                                                                              @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPostRes> postRes = postService.findByCustomerEmail(email, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_SUCCESS, postRes));
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<BaseResponse<Page<Post>>> getAllPostsPaged(@RequestParam int page, @RequestParam int size) {
        Page<Post> postPage = postService.getAllPostsPaged(page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_SUCCESS,postPage));
    }
}
