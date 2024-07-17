package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.popup_review.req.PopupReviewReq;
import com.fiiiiive.zippop.popup_review.res.PopupReviewRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
public class PopupReviewController {

    private final PopupReviewService popupReviewService;

    public PopupReviewController(PopupReviewService popupReviewService) {
        this.popupReviewService = popupReviewService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerReview(@RequestBody PopupReviewReq popupReviewReq) {
        popupReviewService.register(popupReviewReq);

        return ResponseEntity.ok("등록 성공");
    }

    @GetMapping(value = "/search_store_name")
    public ResponseEntity<Optional<List<PopupReviewRes>>> search_store_name(@RequestParam String store_name) {
        Optional<List<PopupReviewRes>> popupReviewResList = popupReviewService.findByStoreName(store_name);

        return ResponseEntity.ok(popupReviewResList);
    }
}
