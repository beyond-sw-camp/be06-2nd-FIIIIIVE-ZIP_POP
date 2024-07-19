package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.CreatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody CreatePostReq createPostReq) {
        postService.register(createPostReq);
        return ResponseEntity.ok("등록 성공");
    }

    @GetMapping(value = "/search-customer-email")
    public ResponseEntity<List<GetPostRes>> searchCustomerEmail(@RequestParam String email) {
        List<GetPostRes> postRes = postService.findByCustomerEmail(email);
        return ResponseEntity.ok(postRes);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<Page<Post>> getAllPostsPaged(@RequestParam int page, @RequestParam int size) {
        Page<Post> postPage = postService.getAllPostsPaged(page, size);
        return ResponseEntity.ok(postPage);
    }
}
