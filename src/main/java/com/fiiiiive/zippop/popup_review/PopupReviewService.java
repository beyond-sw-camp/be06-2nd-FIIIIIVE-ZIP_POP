package com.fiiiiive.zippop.popup_review;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.PopupReviewImage;
import com.fiiiiive.zippop.popup_review.model.request.CreatePopupReviewReq;
import com.fiiiiive.zippop.popup_review.model.response.CreatePopupReviewRes;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewImageRes;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PopupReviewService {
    private final PopupReviewRepository popupReviewRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final PopupReviewImageRepository popupReviewImageRepository;
    private final CustomerRepository customerRepository;

    public CreatePopupReviewRes register(CustomUserDetails customUserDetails, Long storeIdx, List<String> fileNames, CreatePopupReviewReq dto) throws BaseException {
        Customer customer = customerRepository.findByCustomerEmail(customUserDetails.getEmail())
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_INVALID_MEMBER));
        PopupStore popupStore = popupStoreRepository.findById(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST));
        PopupReview popupReview = PopupReview.builder()
                .customerEmail(customUserDetails.getEmail())
                .reviewTitle(dto.getReviewTitle())
                .reviewContent(dto.getReviewContent())
                .rating(dto.getRating())
                .popupStore(popupStore)
                .customer(customer)
                .build();
        popupReviewRepository.save(popupReview);
        List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
        for(String fileName: fileNames) {
            PopupReviewImage popupReviewImage = PopupReviewImage.builder()
                    .imageUrl(fileName)
                    .popupReview(popupReview)
                    .build();
            popupReviewImageRepository.save(popupReviewImage);
            GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                    .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                    .imageUrl(popupReviewImage.getImageUrl())
                    .createdAt(popupReviewImage.getCreatedAt())
                    .updatedAt(popupReviewImage.getUpdatedAt())
                    .build();
            getPopupReviewImageResList.add(getPopupReviewImageRes);
        }
        return CreatePopupReviewRes.builder()
                .reviewIdx(popupReview.getReviewIdx())
                .customerEmail(popupReview.getCustomerEmail())
                .reviewTitle(popupReview.getReviewTitle())
                .reviewContent(popupReview.getReviewContent())
                .rating(popupReview.getRating())
                .getPopupReviewImageResList(getPopupReviewImageResList)
                .createdAt(popupReview.getCreatedAt())
                .updatedAt(popupReview.getUpdatedAt())
                .build();
    }

    public Page<GetPopupReviewRes> searchStore(Long storeIdx, int page, int size) throws BaseException {
        Page<PopupReview> result = popupReviewRepository.findByStoreIdx(storeIdx, PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST));
        Page<GetPopupReviewRes> getPopupReviewResPage = result.map(popupReview -> {
            List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
            List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
            for(PopupReviewImage popupReviewImage : popupReviewImageList){
                GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                    .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                    .imageUrl(popupReviewImage.getImageUrl())
                    .createdAt(popupReviewImage.getCreatedAt())
                    .updatedAt(popupReviewImage.getUpdatedAt())
                    .build();
                getPopupReviewImageResList.add(getPopupReviewImageRes);
            }
            GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                    .reviewIdx(popupReview.getReviewIdx())
                    .customerEmail(popupReview.getCustomerEmail())
                    .reviewTitle(popupReview.getReviewTitle())
                    .reviewContent(popupReview.getReviewContent())
                    .rating(popupReview.getRating())
                    .createdAt(popupReview.getCreatedAt())
                    .updatedAt(popupReview.getUpdatedAt())
                    .getPopupReviewImageResList(getPopupReviewImageResList)
                    .build();
            return getPopupReviewRes;
        });
        return getPopupReviewResPage;
    }

    public Page<GetPopupReviewRes> searchCustomer(CustomUserDetails customUserDetails, int page, int size) throws BaseException {
        Page<PopupReview> result = popupReviewRepository.findByCustomerEmail(customUserDetails.getEmail(), PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST));
        Page<GetPopupReviewRes> getPopupReviewResPage = result.map(popupReview -> {
            List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
            List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
            for(PopupReviewImage popupReviewImage : popupReviewImageList){
                GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                        .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                        .imageUrl(popupReviewImage.getImageUrl())
                        .createdAt(popupReviewImage.getCreatedAt())
                        .updatedAt(popupReviewImage.getUpdatedAt())
                        .build();
                getPopupReviewImageResList.add(getPopupReviewImageRes);
            }
            GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                    .reviewIdx(popupReview.getReviewIdx())
                    .customerEmail(popupReview.getCustomerEmail())
                    .reviewTitle(popupReview.getReviewTitle())
                    .reviewContent(popupReview.getReviewContent())
                    .rating(popupReview.getRating())
                    .createdAt(popupReview.getCreatedAt())
                    .updatedAt(popupReview.getUpdatedAt())
                    .getPopupReviewImageResList(getPopupReviewImageResList)
                    .build();
            return getPopupReviewRes;
        });
        return getPopupReviewResPage;
    }
}