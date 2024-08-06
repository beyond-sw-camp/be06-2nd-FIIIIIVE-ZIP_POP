package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.PopupGoodsImage;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsImageRes;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.PopupReviewImage;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewImageRes;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import com.fiiiiive.zippop.popup_store.model.PopupStoreLike;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.request.UpdatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.CreatePopupStoreRes;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreImageRes;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import com.fiiiiive.zippop.popup_store.model.response.UpdatePopupStoreRes;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.PostImage;
import com.fiiiiive.zippop.post.model.PostLike;
import com.fiiiiive.zippop.post.model.request.UpdatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostImageRes;
import com.fiiiiive.zippop.post.model.response.UpdatePostRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopupStoreService {
    private final PopupStoreRepository popupStoreRepository;
    private final CompanyRepository companyRepository;
    private final PopupStoreImageRepository popupStoreImageRepository;
    private final CustomerRepository customerRepository;
    private final PopupStoreLikeRepository popupStoreLikeRepository;

    public CreatePopupStoreRes register(CustomUserDetails customUserDetails, CreatePopupStoreReq dto, List<String> fileNames) throws BaseException {
        Company company = companyRepository.findByCompanyEmail(customUserDetails.getEmail())
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_REGISTER_FAIL_UNAUTHORIZED));
        PopupStore popupStore = PopupStore.builder()
                .companyEmail(customUserDetails.getEmail())
                .storeName(dto.getStoreName())
                .storeContent(dto.getStoreContent())
                .storeAddress(dto.getStoreAddress())
                .category(dto.getCategory())
                .storeStartDate(dto.getStoreStartDate())
                .storeEndDate(dto.getStoreEndDate())
                .likeCount(0)
                .company(company)
                .build();
        popupStoreRepository.save(popupStore);
        List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
        for(String fileName : fileNames){
            PopupStoreImage popupStoreImage = PopupStoreImage.builder()
                    .imageUrl(fileName)
                    .popupStore(popupStore)
                    .build();
            popupStoreImageRepository.save(popupStoreImage);
            GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                    .storeImageIdx(popupStoreImage.getStoreImageIdx())
                    .imageUrl(popupStoreImage.getImageUrl())
                    .createdAt(popupStoreImage.getCreatedAt())
                    .updatedAt(popupStoreImage.getUpdatedAt())
                    .build();
            getPopupStoreImageResList.add(getPopupStoreImageRes);
        }
        return CreatePopupStoreRes.builder()
                .storeIdx(popupStore.getStoreIdx())
                .companyEmail(popupStore.getCompanyEmail())
                .storeName(popupStore.getStoreName())
                .storeContent(popupStore.getStoreContent())
                .storeAddress(popupStore.getStoreAddress())
                .category(popupStore.getCategory())
                .likeCount(popupStore.getLikeCount())
                .totalPeople(popupStore.getTotalPeople())
                .storeStartDate(popupStore.getStoreStartDate())
                .storeEndDate(popupStore.getStoreEndDate())
                .createdAt(popupStore.getCreatedAt())
                .updatedAt(popupStore.getUpdatedAt())
                .getPopupStoreImageResList(getPopupStoreImageResList)
                .build();
    }

    public GetPopupStoreRes search(Long storeIdx) throws BaseException {
        PopupStore popupStore = popupStoreRepository.findByStoreIdx(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
        List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
        for (PopupStoreImage popupStoreImage : popupStoreImageList) {
            GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                    .storeImageIdx(popupStoreImage.getStoreImageIdx())
                    .imageUrl(popupStoreImage.getImageUrl())
                    .createdAt(popupStoreImage.getCreatedAt())
                    .updatedAt(popupStoreImage.getUpdatedAt())
                    .build();
            getPopupStoreImageResList.add(getPopupStoreImageRes);
        }
        List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
        List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
        for (PopupGoods popupGoods : popupGoodsList) {
            List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
            List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
            for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                        .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                        .imageUrl(popupGoodsImage.getImageUrl())
                        .createdAt(popupGoodsImage.getCreatedAt())
                        .updatedAt(popupGoodsImage.getUpdatedAt())
                        .build();
                getPopupGoodsImageResList.add(getPopupGoodsImageRes);
            }
            GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productAmount(popupGoods.getProductAmount())
                    .createdAt(popupGoods.getCreatedAt())
                    .updatedAt(popupGoods.getUpdatedAt())
                    .getPopupGoodsImageResList(getPopupGoodsImageResList)
                    .build();
            getPopupGoodsResList.add(getPopupGoodsRes);
        }
        List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
        List<PopupReview> popupReviewList = popupStore.getReviewList();
        for (PopupReview popupReview : popupReviewList) {
            List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
            List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
            for(PopupReviewImage popupReviewImage : popupReviewImageList){
                GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                        .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                        .imageUrl(popupReviewImage.getImageUrl())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .build();
                getPopupReviewImageResList.add(getPopupReviewImageRes);
            }
            GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                    .reviewIdx(popupReview.getReviewIdx())
                    .reviewTitle(popupReview.getReviewTitle())
                    .reviewContent(popupReview.getReviewContent())
                    .rating(popupReview.getRating())
                    .createdAt(popupReview.getCreatedAt())
                    .updatedAt(popupReview.getUpdatedAt())
                    .getPopupReviewImageResList(getPopupReviewImageResList)
                    .build();
            getPopupReviewResList.add(getPopupReviewRes);
        }
        GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                .storeIdx(popupStore.getStoreIdx())
                .companyEmail(popupStore.getCompanyEmail())
                .storeName(popupStore.getStoreName())
                .storeContent(popupStore.getStoreContent())
                .storeAddress(popupStore.getStoreAddress())
                .category(popupStore.getCategory())
                .likeCount(popupStore.getLikeCount())
                .totalPeople(popupStore.getTotalPeople())
                .storeStartDate(popupStore.getStoreStartDate())
                .storeEndDate(popupStore.getStoreEndDate())
                .getPopupGoodsResList(getPopupGoodsResList)
                .getPopupReviewResList(getPopupReviewResList)
                .getPopupStoreImageResList(getPopupStoreImageResList)
                .build();
            return getPopupStoreRes;
    }

    public Page<GetPopupStoreRes> searchAll(int page, int size) throws BaseException {
        Page<PopupStore> result = popupStoreRepository.findAll(PageRequest.of(page, size));
        if (!result.hasContent()) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
        Page<GetPopupStoreRes> getPopupStoreResList = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResList;
    }

    public Page<GetPopupStoreRes> searchCompany(CustomUserDetails customUserDetails, int page, int size) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByCompanyEmail(customUserDetails.getEmail(), PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        Page<GetPopupStoreRes> getPopupStoreResList = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResList;
    }

    public Page<GetPopupStoreRes> searchKeyword(String keyword, int page, int size) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByKeyword(keyword, PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        Page<GetPopupStoreRes> getPopupStoreResList = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
            for (PopupGoods popupGoods : popupGoodsList) {
                List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
                List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
                for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                    GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                            .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                            .imageUrl(popupGoodsImage.getImageUrl())
                            .createdAt(popupGoodsImage.getCreatedAt())
                            .updatedAt(popupGoodsImage.getUpdatedAt())
                            .build();
                    getPopupGoodsImageResList.add(getPopupGoodsImageRes);
                }
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productAmount(popupGoods.getProductAmount())
                        .createdAt(popupGoods.getCreatedAt())
                        .updatedAt(popupGoods.getUpdatedAt())
                        .getPopupGoodsImageResList(getPopupGoodsImageResList)
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
            List<PopupReview> popupReviewList = popupStore.getReviewList();
            for (PopupReview popupReview : popupReviewList) {
                List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
                List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
                for(PopupReviewImage popupReviewImage : popupReviewImageList){
                    GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                            .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                            .imageUrl(popupReviewImage.getImageUrl())
                            .createdAt(popupReview.getCreatedAt())
                            .updatedAt(popupReview.getUpdatedAt())
                            .build();
                    getPopupReviewImageResList.add(getPopupReviewImageRes);
                }
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewIdx(popupReview.getReviewIdx())
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .getPopupReviewImageResList(getPopupReviewImageResList)
                        .build();
                getPopupReviewResList.add(getPopupReviewRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupGoodsResList(getPopupGoodsResList)
                    .getPopupReviewResList(getPopupReviewResList)
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResList;
    }

    public Page<GetPopupStoreRes> searchDateRange(LocalDateTime storeStartDate, LocalDateTime storeEndDate, int page, int size) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByStoreDateRange(storeStartDate, storeEndDate, PageRequest.of(page, size))
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        Page<GetPopupStoreRes> getPopupStoreResList = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
            for (PopupGoods popupGoods : popupGoodsList) {
                List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
                List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
                for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                    GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                            .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                            .imageUrl(popupGoodsImage.getImageUrl())
                            .createdAt(popupGoodsImage.getCreatedAt())
                            .updatedAt(popupGoodsImage.getUpdatedAt())
                            .build();
                    getPopupGoodsImageResList.add(getPopupGoodsImageRes);
                }
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productAmount(popupGoods.getProductAmount())
                        .createdAt(popupGoods.getCreatedAt())
                        .updatedAt(popupGoods.getUpdatedAt())
                        .getPopupGoodsImageResList(getPopupGoodsImageResList)
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
            List<PopupReview> popupReviewList = popupStore.getReviewList();
            for (PopupReview popupReview : popupReviewList) {
                List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
                List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
                for(PopupReviewImage popupReviewImage : popupReviewImageList){
                    GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                            .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                            .imageUrl(popupReviewImage.getImageUrl())
                            .createdAt(popupReview.getCreatedAt())
                            .updatedAt(popupReview.getUpdatedAt())
                            .build();
                    getPopupReviewImageResList.add(getPopupReviewImageRes);
                }
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewIdx(popupReview.getReviewIdx())
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .getPopupReviewImageResList(getPopupReviewImageResList)
                        .build();
                getPopupReviewResList.add(getPopupReviewRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupGoodsResList(getPopupGoodsResList)
                    .getPopupReviewResList(getPopupReviewResList)
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResList;
    }

    public UpdatePopupStoreRes update(CustomUserDetails customUserDetails, Long storeIdx, UpdatePopupStoreReq dto, List<String> fileNames) throws BaseException {
        PopupStore popupStore = popupStoreRepository.findByStoreIdx(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        if(!Objects.equals(popupStore.getCompanyEmail(), customUserDetails.getEmail())) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_UPDATE_FAIL_INVALID_MEMBER);
        }
        popupStore.setStoreName(dto.getStoreName());
        popupStore.setStoreContent(dto.getStoreContent());
        popupStore.setStoreAddress(dto.getStoreAddress());
        popupStore.setCategory(dto.getCategory());
        popupStore.setTotalPeople(dto.getTotalPeople());
        popupStore.setStoreEndDate(dto.getStoreEndDate());
        popupStore.setStoreEndDate(dto.getStoreEndDate());
        popupStoreRepository.save(popupStore);
        List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
        List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
        for(PopupStoreImage popupStoreImage : popupStoreImageList){
            popupStoreImageRepository.deleteById(popupStoreImage.getStoreImageIdx());
        }
        for (String fileName: fileNames) {
            PopupStoreImage popupstoreImage = PopupStoreImage.builder()
                    .imageUrl(fileName)
                    .popupStore(popupStore)
                    .build();
            popupStoreImageRepository.save(popupstoreImage);
            GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                    .storeImageIdx(popupstoreImage.getStoreImageIdx())
                    .imageUrl(popupstoreImage.getImageUrl())
                    .createdAt(popupstoreImage.getCreatedAt())
                    .updatedAt(popupstoreImage.getUpdatedAt())
                    .build();
            getPopupStoreImageResList.add(getPopupStoreImageRes);
        }
        return UpdatePopupStoreRes.builder()
                .storeIdx(popupStore.getStoreIdx())
                .companyEmail(popupStore.getCompanyEmail())
                .storeName(popupStore.getStoreName())
                .storeContent(popupStore.getStoreContent())
                .storeAddress(popupStore.getStoreAddress())
                .category(popupStore.getCategory())
                .likeCount(popupStore.getLikeCount())
                .totalPeople(popupStore.getTotalPeople())
                .storeStartDate(popupStore.getStoreStartDate())
                .storeEndDate(popupStore.getStoreEndDate())
                .createdAt(popupStore.getCreatedAt())
                .updatedAt(popupStore.getUpdatedAt())
                .getPopupStoreImageResList(getPopupStoreImageResList)
                .build();
    }

    public void delete(CustomUserDetails customUserDetails, Long storeIdx) throws BaseException{
        PopupStore popupStore = popupStoreRepository.findByStoreIdx(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_DELETE_FAIL_NOT_FOUND));
        if(!Objects.equals(popupStore.getCompanyEmail(), customUserDetails.getEmail())){
            throw new BaseException(BaseResponseMessage.POPUP_STORE_DELETE_FAIL_INVALID_MEMBER);
        }
        popupStoreRepository.deleteById(storeIdx);
    }

    @Transactional
    public void like(CustomUserDetails customUserDetails, Long storeIdx) throws BaseException{
        PopupStore popupStore = popupStoreRepository.findByStoreIdx(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_LIKE_FAIL_NOT_FOUND));
        Customer customer = customerRepository.findById(customUserDetails.getIdx())
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_LIKE_FAIL_INVALID_MEMBER));
        Optional<PopupStoreLike> result = popupStoreLikeRepository.findByCustomerEmailAndStoreIdx(customUserDetails.getEmail(),storeIdx);
        if (result.isEmpty()) {
            popupStore.setLikeCount(popupStore.getLikeCount() + 1);
            popupStoreRepository.save(popupStore);
            PopupStoreLike popupStoreLike = PopupStoreLike.builder()
                    .popupStore(popupStore)
                    .customer(customer)
                    .build();
            popupStoreLikeRepository.save(popupStoreLike);
        } else {
            popupStore.setLikeCount(popupStore.getLikeCount() - 1);
            popupStoreRepository.save(popupStore);
            popupStoreLikeRepository.deleteByCustomerEmailAndStoreIdx(customer.getEmail(), storeIdx);
        }
    }

    public Page<GetPopupStoreRes> searchCategory(String category, int page, int size) throws BaseException {
        Page<PopupStore> result = popupStoreRepository.findByCategory(category, PageRequest.of(page, size))
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        Page<GetPopupStoreRes> getPopupStoreResList = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
            for (PopupGoods popupGoods : popupGoodsList) {
                List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
                List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
                for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                    GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                            .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                            .imageUrl(popupGoodsImage.getImageUrl())
                            .createdAt(popupGoodsImage.getCreatedAt())
                            .updatedAt(popupGoodsImage.getUpdatedAt())
                            .build();
                    getPopupGoodsImageResList.add(getPopupGoodsImageRes);
                }
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productAmount(popupGoods.getProductAmount())
                        .createdAt(popupGoods.getCreatedAt())
                        .updatedAt(popupGoods.getUpdatedAt())
                        .getPopupGoodsImageResList(getPopupGoodsImageResList)
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
            List<PopupReview> popupReviewList = popupStore.getReviewList();
            for (PopupReview popupReview : popupReviewList) {
                List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
                List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
                for(PopupReviewImage popupReviewImage : popupReviewImageList){
                    GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                            .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                            .imageUrl(popupReviewImage.getImageUrl())
                            .createdAt(popupReview.getCreatedAt())
                            .updatedAt(popupReview.getUpdatedAt())
                            .build();
                    getPopupReviewImageResList.add(getPopupReviewImageRes);
                }
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewIdx(popupReview.getReviewIdx())
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .getPopupReviewImageResList(getPopupReviewImageResList)
                        .build();
                getPopupReviewResList.add(getPopupReviewRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupGoodsResList(getPopupGoodsResList)
                    .getPopupReviewResList(getPopupReviewResList)
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResList;
    }

    public GetPopupStoreRes searchStore(String storeName) throws BaseException{
        PopupStore popupStore = popupStoreRepository.findByStoreName(storeName)
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST));
        List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
        List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
        for (PopupStoreImage popupStoreImage : popupStoreImageList) {
            GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                    .storeImageIdx(popupStoreImage.getStoreImageIdx())
                    .imageUrl(popupStoreImage.getImageUrl())
                    .createdAt(popupStoreImage.getCreatedAt())
                    .updatedAt(popupStoreImage.getUpdatedAt())
                    .build();
            getPopupStoreImageResList.add(getPopupStoreImageRes);
        }
        List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
        List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
        for (PopupGoods popupGoods : popupGoodsList) {
            List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
            List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
            for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                        .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                        .imageUrl(popupGoodsImage.getImageUrl())
                        .createdAt(popupGoodsImage.getCreatedAt())
                        .updatedAt(popupGoodsImage.getUpdatedAt())
                        .build();
                getPopupGoodsImageResList.add(getPopupGoodsImageRes);
            }
            GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productAmount(popupGoods.getProductAmount())
                    .createdAt(popupGoods.getCreatedAt())
                    .updatedAt(popupGoods.getUpdatedAt())
                    .getPopupGoodsImageResList(getPopupGoodsImageResList)
                    .build();
            getPopupGoodsResList.add(getPopupGoodsRes);
        }
        List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
        List<PopupReview> popupReviewList = popupStore.getReviewList();
        for (PopupReview popupReview : popupReviewList) {
            List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
            List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
            for(PopupReviewImage popupReviewImage : popupReviewImageList){
                GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                        .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                        .imageUrl(popupReviewImage.getImageUrl())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .build();
                getPopupReviewImageResList.add(getPopupReviewImageRes);
            }
            GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                    .reviewIdx(popupReview.getReviewIdx())
                    .reviewTitle(popupReview.getReviewTitle())
                    .reviewContent(popupReview.getReviewContent())
                    .rating(popupReview.getRating())
                    .createdAt(popupReview.getCreatedAt())
                    .updatedAt(popupReview.getUpdatedAt())
                    .getPopupReviewImageResList(getPopupReviewImageResList)
                    .build();
            getPopupReviewResList.add(getPopupReviewRes);
        }
        GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                .storeIdx(popupStore.getStoreIdx())
                .companyEmail(popupStore.getCompanyEmail())
                .storeName(popupStore.getStoreName())
                .storeContent(popupStore.getStoreContent())
                .storeAddress(popupStore.getStoreAddress())
                .category(popupStore.getCategory())
                .likeCount(popupStore.getLikeCount())
                .totalPeople(popupStore.getTotalPeople())
                .storeStartDate(popupStore.getStoreStartDate())
                .storeEndDate(popupStore.getStoreEndDate())
                .getPopupGoodsResList(getPopupGoodsResList)
                .getPopupReviewResList(getPopupReviewResList)
                .getPopupStoreImageResList(getPopupStoreImageResList)
                .build();
        return getPopupStoreRes;
    }

    public Page<GetPopupStoreRes> searchAddress(String storeAddress, int page, int size) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByStoreAddress(storeAddress, PageRequest.of(page, size));
        if (!result.hasContent()) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
        Page<GetPopupStoreRes> getPopupStoreResPage = result.map(popupStore -> {
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            for (PopupStoreImage popupStoreImage : popupStoreImageList) {
                GetPopupStoreImageRes getPopupStoreImageRes = GetPopupStoreImageRes.builder()
                        .storeImageIdx(popupStoreImage.getStoreImageIdx())
                        .imageUrl(popupStoreImage.getImageUrl())
                        .createdAt(popupStoreImage.getCreatedAt())
                        .updatedAt(popupStoreImage.getUpdatedAt())
                        .build();
                getPopupStoreImageResList.add(getPopupStoreImageRes);
            }
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            List<PopupGoods> popupGoodsList = popupStore.getPopupGoodsList();
            for (PopupGoods popupGoods : popupGoodsList) {
                List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
                List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
                for(PopupGoodsImage popupGoodsImage: popupGoodsImageList){
                    GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                            .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                            .imageUrl(popupGoodsImage.getImageUrl())
                            .createdAt(popupGoodsImage.getCreatedAt())
                            .updatedAt(popupGoodsImage.getUpdatedAt())
                            .build();
                    getPopupGoodsImageResList.add(getPopupGoodsImageRes);
                }
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productAmount(popupGoods.getProductAmount())
                        .createdAt(popupGoods.getCreatedAt())
                        .updatedAt(popupGoods.getUpdatedAt())
                        .getPopupGoodsImageResList(getPopupGoodsImageResList)
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            List<GetPopupReviewRes> getPopupReviewResList = new ArrayList<>();
            List<PopupReview> popupReviewList = popupStore.getReviewList();
            for (PopupReview popupReview : popupReviewList) {
                List<PopupReviewImage> popupReviewImageList = popupReview.getPopupReviewImageList();
                List<GetPopupReviewImageRes> getPopupReviewImageResList = new ArrayList<>();
                for(PopupReviewImage popupReviewImage : popupReviewImageList){
                    GetPopupReviewImageRes getPopupReviewImageRes = GetPopupReviewImageRes.builder()
                            .reviewImageIdx(popupReviewImage.getReviewImageIdx())
                            .imageUrl(popupReviewImage.getImageUrl())
                            .createdAt(popupReview.getCreatedAt())
                            .updatedAt(popupReview.getUpdatedAt())
                            .build();
                    getPopupReviewImageResList.add(getPopupReviewImageRes);
                }
                GetPopupReviewRes getPopupReviewRes = GetPopupReviewRes.builder()
                        .reviewIdx(popupReview.getReviewIdx())
                        .reviewTitle(popupReview.getReviewTitle())
                        .reviewContent(popupReview.getReviewContent())
                        .rating(popupReview.getRating())
                        .createdAt(popupReview.getCreatedAt())
                        .updatedAt(popupReview.getUpdatedAt())
                        .getPopupReviewImageResList(getPopupReviewImageResList)
                        .build();
                getPopupReviewResList.add(getPopupReviewRes);
            }
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeIdx(popupStore.getStoreIdx())
                    .companyEmail(popupStore.getCompanyEmail())
                    .storeName(popupStore.getStoreName())
                    .storeContent(popupStore.getStoreContent())
                    .storeAddress(popupStore.getStoreAddress())
                    .category(popupStore.getCategory())
                    .likeCount(popupStore.getLikeCount())
                    .totalPeople(popupStore.getTotalPeople())
                    .storeStartDate(popupStore.getStoreStartDate())
                    .storeEndDate(popupStore.getStoreEndDate())
                    .getPopupGoodsResList(getPopupGoodsResList)
                    .getPopupReviewResList(getPopupReviewResList)
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            return getPopupStoreRes;
        });
        return getPopupStoreResPage;
    }
}