package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.request.PopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.PopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PopupReviewService {

    private final PopupReviewRepository popupReviewRepository;
    private final PopupStoreRepository popupStoreRepository;

    public PopupReviewService(PopupReviewRepository popupReviewRepository, PopupStoreRepository popupStoreRepository) {
        this.popupReviewRepository = popupReviewRepository;
        this.popupStoreRepository = popupStoreRepository;
    }

    public void register(PopupReviewReq popupReviewReq) throws BaseException{
        PopupReview popupReview = PopupReview.builder()
                .reviewTitle(popupReviewReq.getReviewTitle())
                .reviewContent(popupReviewReq.getReviewContent())
                .reviewDate(popupReviewReq.getReviewDate())
                .rating(popupReviewReq.getRating())
                .popupStore(popupStoreRepository.findByStoreName(popupReviewReq.getStoreName()))
                .build();

        PopupStore optionalStore = popupStoreRepository.findByStoreName(popupReviewReq.getStoreName());
        if (optionalStore != null) {
            popupReview.setPopupStore(optionalStore);

            popupReviewRepository.save(popupReview);

            optionalStore.getReviews().add(popupReview);
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_CONTENTS_EMPTY);
        }
    }

//    public Optional<List<PopupReviewRes>> findAll() {
//        List<PopupReview> popupReviewList = popupReviewRepository.findAll();
//        if (popupReviewList.isEmpty()) {
//            return Optional.empty();
//        }
//        List<PopupReviewRes> popupReviewResList = new ArrayList<>();
//        for (PopupReview popupReview : popupReviewList) {
//            PopupReviewRes popupReviewRes = convertToReviewRes(popupReview);
//            popupReviewResList.add(popupReviewRes);
//        }
//
//        return Optional.of(popupReviewResList);
//    }

    public Optional<List<PopupReviewRes>> findByStoreName(String store_name) {
//        Optional<PopupReview> result = popupReviewRepository.findByStoreName(store_name);
//        PopupReview popupReview = result.get();
        List<PopupReview> popupReviewList = popupReviewRepository.findByStoreName(store_name);
        if (popupReviewList.isEmpty()) {
            return Optional.empty();
        }
        List<PopupReviewRes> popupReviewResList = new ArrayList<>();
        for (PopupReview popupReview : popupReviewList) {
            PopupReviewRes popupReviewRes = new PopupReviewRes();
            popupReviewRes = popupReviewRes.convertToReviewRes(popupReview);
            popupReviewRes.setStoreName(store_name);
            popupReviewResList.add(popupReviewRes);
        }

        return Optional.of(popupReviewResList);
    }

//    private PopupReviewRes convertToReviewRes(PopupReview review) {
//        PopupReviewRes popupReviewRes = new PopupReviewRes();
//        popupReviewRes.setReviewTitle(review.getReviewTitle());
//        popupReviewRes.setReviewContent(review.getReviewContent());
//        popupReviewRes.setRating(review.getRating());
//        popupReviewRes.setReviewDate(review.getReviewDate());
//        popupReviewRes.setStoreName(review.getStoreName());
//        return popupReviewRes;
//    }
}
