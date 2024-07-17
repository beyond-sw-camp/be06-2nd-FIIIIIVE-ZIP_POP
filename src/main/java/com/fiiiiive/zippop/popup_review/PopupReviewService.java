package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.popup_review.req.PopupReviewReq;
import com.fiiiiive.zippop.popup_review.res.PopupReviewRes;
import com.fiiiiive.zippop.popup_store.PopupStore;
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

    public void register(PopupReviewReq popupReviewReq) {
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
            throw new RuntimeException("store not found");
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
        List<PopupReview> popupReviewList = popupReviewRepository.findByStoreName(store_name);
        if (popupReviewList.isEmpty()) {
            return Optional.empty();
        }
        List<PopupReviewRes> popupReviewResList = new ArrayList<>();
        for (PopupReview popupReview : popupReviewList) {
            PopupReviewRes popupReviewRes = convertToReviewRes(popupReview);
            popupReviewRes.setStoreName(store_name);
            popupReviewResList.add(popupReviewRes);
        }

        return Optional.of(popupReviewResList);
    }

    private PopupReviewRes convertToReviewRes(PopupReview review) {
        PopupReviewRes popupReviewRes = new PopupReviewRes();
        popupReviewRes.setReviewTitle(review.getReviewTitle());
        popupReviewRes.setReviewContent(review.getReviewContent());
        popupReviewRes.setRating(review.getRating());
        popupReviewRes.setReviewDate(review.getReviewDate());
        popupReviewRes.setStoreName(review.getStoreName());
        return popupReviewRes;
    }
}
