package com.fiiiiive.zippop.popup_review;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.CreatePopupReviewRes;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.utils.CloudFileUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "popup-review-api", description = "PopupReview")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class PopupReviewController {
    private final PopupReviewService popupReviewService;
    private final CloudFileUpload cloudFileUpload;

    // 리뷰 등록
    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long storeIdx,
        @RequestPart("files") MultipartFile[] files,
        @RequestPart("dto") CreatePopupReviewReq dto) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        CreatePopupReviewRes response = popupReviewService.register(customUserDetails, storeIdx, fileNames, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_REGISTER_SUCCESS, response));
    }

    // 스토어의 인덱스 번호로 등록된 리뷰 조회
    @GetMapping(value = "/search-store")
    public ResponseEntity<BaseResponse<Page<GetPopupReviewRes>>> searchStore(
        @RequestParam Long storeIdx,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupReviewRes> response = popupReviewService.searchStore(storeIdx, page, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, response));
    }

    // 자신이 쓴 리뷰 조회
    @GetMapping(value = "/search-customer")
    public ResponseEntity<BaseResponse<Page<GetPopupReviewRes>>> searchCustomer(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupReviewRes> response = popupReviewService.searchCustomer(customUserDetails, page, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, response));
    }
}