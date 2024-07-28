package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.annotation.ExeTimer;
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
import retrofit2.http.Multipart;

import java.util.List;

@Tag(name = "post-api", description = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final CloudFileUpload cloudFileUpload;

    // 게시글 생성
    @PostMapping(value = "/register")
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
    @GetMapping(value = "/search")
    public ResponseEntity<BaseResponse<GetPostRes>> searchByIdx(
            @RequestParam Long postIdx) throws BaseException {
        GetPostRes response = postService.search(postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_IDX_SUCCESS, response));
    }

    // 게시글 전체 조회
    // @ExeTimer
    @GetMapping(value = "/search-all")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchAll(
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchAll(page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_ALL_SUCCESS, response));
    }

    // 게시글 검색어 추천 조회
    // @ExeTimer
    @GetMapping(value = "/search-recommend")
    public ResponseEntity<BaseResponse<List<GetPostRes>>> searchRecommend(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String keyword) throws BaseException {
        Page<GetPostRes> response = postService.searchRecommend(page, size, keyword);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_KEYWORD_SUCCESS, response));
    }

    // 게시글 고객 회원 조회
    // @ExeTimer
    @GetMapping(value = "/search-customer")
    public ResponseEntity<BaseResponse<Page<GetPostRes>>> searchByCustomer(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPostRes> response = postService.searchCustomer(customUserDetails, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_SEARCH_BY_CUSTOMER_SUCCESS, response));
    }

    // 게시글 수정
    @PatchMapping(value = "/update")
    public ResponseEntity<BaseResponse> updatePost(
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
    public ResponseEntity<BaseResponse> deleteByCartIdx(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx) throws BaseException {
        postService.delete(customUserDetails, postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_DELETE_SUCCESS));
    }

    // 게시글 추천: postLikeCount에 대한 동시성 제어는 자원소모에 비해 중요도가 떨어지므로 하지 않는다.
    @GetMapping("/like")
    public ResponseEntity<BaseResponse> adjustLike(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long postIdx,
        @RequestParam Long operation) throws BaseException {
        postService.adjustLike(customUserDetails, postIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POST_LIKE_SUCCESS));
    }
}
