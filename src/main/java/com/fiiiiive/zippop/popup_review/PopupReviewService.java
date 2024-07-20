package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PopupReviewService {

    private final PopupReviewRepository popupReviewRepository;
    private final PopupStoreRepository popupStoreRepository;

    public void register(CreatePopupReviewReq createPopupReviewReq) {
        PopupReview popupReview = PopupReview.builder()
                .reviewTitle(createPopupReviewReq.getReviewTitle())
                .reviewContent(createPopupReviewReq.getReviewContent())
                .reviewDate(createPopupReviewReq.getReviewDate())
                .rating(createPopupReviewReq.getRating())
                .popupStore(popupStoreRepository.findByStoreName(createPopupReviewReq.getStoreName()).get())
                .build();

        Optional<PopupStore> popupStore = Optional.of(popupStoreRepository.findByStoreName(createPopupReviewReq.getStoreName()).get());
        if (popupStore.isPresent()) {
            popupReview.setPopupStore(popupStore.get());
            popupReview.setStoreName(popupStore.get().getStoreName());
            popupReviewRepository.save(popupReview);

            popupStore.get().getReviews().add(popupReview);
        } else {
            throw new RuntimeException("store not found");
        }
    }

    public List<GetPopupReviewRes> findAll() {
        Optional<List<PopupReview>> result = Optional.of(popupReviewRepository.findAll());
        if (result.isPresent()) {
            List<GetPopupReviewRes> popupReviewResList = new ArrayList<>();
            for (PopupReview popupReview : result.get()) {
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .reviewDate(popupReview.getReviewDate())
                        .storeName(popupReview.getPopupStore().getStoreName())
                        .build();
                popupReviewResList.add(getPopupReviewRes);
            }

            return popupReviewResList;
        } else {
            throw new RuntimeException("popup reviews not found");
        }
    }

    public List<GetPopupReviewRes> findByStoreName(String storeName) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupReview>> result = popupReviewRepository.findByStoreName(storeName);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
            for (PopupReview popupReview : result.get()) {
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .reviewDate(popupReview.getReviewDate())
                        .storeName(popupReview.getPopupStore().getStoreName())
//                        .storeName(popupReview.getStoreName())
                        .build();
                getPopupReviewResList.add(getPopupReviewRes);
            }
            start = System.currentTimeMillis();
            result = popupReviewRepository.findByStoreNameWithStore(storeName);
            end = System.currentTimeMillis();
            diff = end - start;
            return getPopupReviewResList;

        } else {
            throw new RuntimeException("store not found");
        }
    }


}