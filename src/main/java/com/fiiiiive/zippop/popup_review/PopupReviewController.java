package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.popup_review.model.request.PopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.PopupReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class PopupReviewController {

    private final PopupReviewService popupReviewService;

//    public PopupReviewController(PopupReviewService popupReviewService) {
//        this.popupReviewService = popupReviewService;
//    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerReview(@RequestBody PopupReviewReq popupReviewReq) throws BaseException {
        popupReviewService.register(popupReviewReq);

        return ResponseEntity.ok("등록 성공");
    }

    @GetMapping(value = "/search_store_name")
    public ResponseEntity<Optional<List<PopupReviewRes>>> search_store_name(@RequestParam String store_name) throws BaseException {
        Optional<List<PopupReviewRes>> popupReviewResList = popupReviewService.findByStoreName(store_name);
        return ResponseEntity.ok(popupReviewResList);
    }
}
