package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name = "popup-review-api", description = "PopupReview")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class PopupReviewController {

    private final PopupReviewService popupReviewService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerReview(@RequestBody CreatePopupReviewReq createPopupReviewReq) {
        popupReviewService.register(createPopupReviewReq);
        return ResponseEntity.ok("등록 성공");
    }

    @ExeTimer
    @GetMapping(value = "/search-store-name")
    public ResponseEntity<List<GetPopupReviewRes>> search_store_name(@RequestParam String storeName) {
        List<GetPopupReviewRes> popupReviewResList = popupReviewService.findByStoreName(storeName);
        return ResponseEntity.ok(popupReviewResList);
    }
}
