package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.post.model.request.UpdatePostReq;
import com.fiiiiive.zippop.post.model.response.CreatePostRes;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import com.fiiiiive.zippop.post.model.response.UpdatePostRes;
import com.fiiiiive.zippop.utils.CloudFileUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "post-api", description = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final CloudFileUpload cloudFileUpload;

    // 게시글 생성
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestPart(name = "dto") CreatePostRes dto,
        @RequestPart(name = "files") MultipartFile[] files) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        CreatePostRes response = postService.register(customUserDetails, fileNames, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_REGISTER_SUCCESS, response));
    }

    // 게시글 단일 조회
    // @ExeTimer
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<GetPostRes>> search(
        @RequestParam Long postIdx) throws BaseException {
        GetPostRes response = postService.search(postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_IDX_SUCCESS, response));
    }

    // 게시글 전체 조회
    // @ExeTimer
    @GetMapping("/search-all")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchAll(
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchAll(page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_ALL_SUCCESS, response));
    }

    // 게시글 키워드 검색
    // @ExeTimer
    @GetMapping("/search-keyword")
    public ResponseEntity<BaseResponse<List<GetPostRes>>> searchKeyword(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String keyword) throws BaseException {
        Page<GetPostRes> response = postService.searchKeyword(keyword, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_KEYWORD_SUCCESS, response));
    }

    // 고객 기반 게시물 조회
    // @ExeTimer
    @GetMapping("/search-customer")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchCustomer(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchCustomer(customUserDetails, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_CUSTOMER_SUCCESS, response));
    }

    // 게시글 수정
    @PatchMapping("/update")
    public ResponseEntity<BaseResponse> update(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx,
        @RequestPart(name = "dto") UpdatePostReq dto,
        @RequestPart(name = "files") MultipartFile[] files) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        UpdatePostRes response = postService.update(customUserDetails, postIdx, dto, fileNames);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_UPDATE_SUCCESS,response));
    }

    // 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx) throws BaseException {
        postService.delete(customUserDetails, postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_DELETE_SUCCESS));
    }

    // 게시글 좋아요
    @GetMapping("/like")
    public ResponseEntity<BaseResponse> like(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx) throws BaseException {
        postService.like(customUserDetails, postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_LIKE_SUCCESS));
    }
}
