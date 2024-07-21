package com.fiiiiive.zippop.popup_review;

import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class PopupReviewController {

    private final PopupReviewService popupReviewService;

    @PostMapping(value = "/register")
    public ResponseEntity<BaseResponse> registerReview(@RequestBody CreatePopupReviewReq createPopupReviewReq) throws Exception{
        popupReviewService.register(createPopupReviewReq);

        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_REGISTER_SUCCESS));
    }

    @GetMapping(value = "/search-store-name")
    public ResponseEntity<BaseResponse<Page<GetPopupReviewRes>>> search_store_name(@RequestParam String storeName,
                                                                                   @RequestParam int page,
                                                                                   @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupReviewRes> popupReviewResList = popupReviewService.findByStoreName(storeName, pageable);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupReviewResList));
    }
}
