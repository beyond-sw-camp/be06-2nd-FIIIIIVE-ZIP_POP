package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.post.model.request.PostReq;
import com.fiiiiive.zippop.post.model.response.PostRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody PostReq postReq) {
        postService.register(postReq);
        return ResponseEntity.ok("등록 성공");
    }

    @GetMapping(value = "/search_customer_email")
    public ResponseEntity<Optional<List<PostRes>>> searchCustomerEmail(@RequestParam String email) {
        Optional<List<PostRes>> postRes = postService.findByCustomerEmail(email);
        return ResponseEntity.ok(postRes);
    }
}
