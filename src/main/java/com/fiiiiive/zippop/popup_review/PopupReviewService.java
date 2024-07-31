package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PopupReviewService {

    private final PopupReviewRepository popupReviewRepository;
    private final PopupStoreRepository popupStoreRepository;

    public void register(CreatePopupReviewReq createPopupReviewReq) throws BaseException {
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

            popupStore.get().getReviewList().add(popupReview);
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST);
        }
    }

    public List<GetPopupReviewRes> findAll() throws BaseException {
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
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_CONTENTS_EMPTY);
        }
    }

    public Page<GetPopupReviewRes> findByStoreName(String storeName, Pageable pageable) throws BaseException {
        Page<PopupReview> result = popupReviewRepository.findByStoreName(storeName, pageable);

        if (result.hasContent()) {
            Page<GetPopupReviewRes> getPopupReviewResPage = result.map(popupReview -> GetPopupReviewRes.builder()
                    .reviewTitle(popupReview.getReviewTitle())
                    .reviewContent(popupReview.getReviewContent())
                    .rating(popupReview.getRating())
                    .reviewDate(popupReview.getReviewDate())
                    .storeName(popupReview.getPopupStore().getStoreName())
                    .build());
            result = popupReviewRepository.findByStoreNameFetchJoin(storeName, pageable);
            return getPopupReviewResPage;

        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST);
        }
    }


}