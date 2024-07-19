package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;


import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PopupStoreService {
    private final PopupStoreRepository popupStoreRepository;
    private final CompanyRepository companyRepository;

    public void register(CustomUserDetails customUserDetails, CreatePopupStoreReq createPopupStoreReq) {
        Company company = companyRepository.findById(customUserDetails.getIdx())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        PopupStore popupStore = PopupStore.builder()
                .storeName(createPopupStoreReq.getStoreName())
                .storeAddr(createPopupStoreReq.getStoreAddr())
                .storeDate(createPopupStoreReq.getStoreDate())
                .category(createPopupStoreReq.getCategory())
                .company(company)
                .build();
        System.out.println(popupStore);
        popupStoreRepository.save(popupStore);
    }

    public List<GetPopupStoreRes> findAll() {
        Optional<List<PopupStore>> result = Optional.of(popupStoreRepository.findAll());
        if (result.isPresent()) {
            List<GetPopupStoreRes> getPopupStoreResList = new ArrayList<>();
            for (PopupStore popupStore : result.get()) {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();
                List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
                for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                    GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                    getPopupGoodsResList.add(getPopupGoodsRes);
                }
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> reviewResList = new ArrayList<>();
                for (PopupReview popupReview : popupStore.getReviews()) {
                    GetPopupReviewRes popupReviewRes = GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                    reviewResList.add(popupReviewRes);
                }
                getPopupStoreRes.setReviews(reviewResList);
                getPopupStoreResList.add(getPopupStoreRes);
            }
            return getPopupStoreResList;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }

    public List<GetPopupStoreRes> findByCategory(String category) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupStore>> result = popupStoreRepository.findByCategory(category);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupStoreRes> getPopupStoreResList = new ArrayList<>();
            for (PopupStore popupStore : result.get()) {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();
                List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
                for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                    GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                    getPopupGoodsResList.add(getPopupGoodsRes);
                }
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);
                List<GetPopupReviewRes> popupReviewResList = new ArrayList<>();
                for (PopupReview popupReview : popupStore.getReviews()) {
                    GetPopupReviewRes popupReviewRes = GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                    popupReviewResList.add(popupReviewRes);
                }
                getPopupStoreRes.setReviews(popupReviewResList);

                getPopupStoreResList.add(getPopupStoreRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");

            start = System.currentTimeMillis();
            result = popupStoreRepository.findByCategoryWithGoods(category);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupStoreResList;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }

    public GetPopupStoreRes findByStoreName(String storeName) {
        Long start = System.currentTimeMillis();
        Optional<PopupStore> result = popupStoreRepository.findByStoreName(storeName);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeName(result.get().getStoreName())
                    .storeAddr(result.get().getStoreAddr())
                    .storeDate(result.get().getStoreDate())
                    .category(result.get().getCategory())
                    .build();
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get().getPopupGoodsList()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeName(popupGoods.getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

            List<GetPopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : result.get().getReviews()) {
                GetPopupReviewRes reviewRes = GetPopupReviewRes.builder()
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .rating(review.getRating())
                        .reviewDate(review.getReviewDate())
                        .storeName(review.getPopupStore().getStoreName())
                        .build();
                reviewResList.add(reviewRes);
            }
            getPopupStoreRes.setReviews(reviewResList);
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = popupStoreRepository.findByStoreNameWithGoods(storeName);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupStoreRes;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }

    public List<GetPopupStoreRes> findByStoreAddr(String storeAddr) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupStore>> result = popupStoreRepository.findByStoreAddr(storeAddr);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupStoreRes> getPopupStoreResList = new ArrayList<>();
            for (PopupStore popupStore : result.get()) {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();
                List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
                for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                    GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                    getPopupGoodsResList.add(getPopupGoodsRes);
                }
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);
                List<GetPopupReviewRes> popupReviewResList = new ArrayList<>();
                for (PopupReview popupReview : popupStore.getReviews()) {
                    GetPopupReviewRes popupReviewRes = GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                    popupReviewResList.add(popupReviewRes);
                }
                getPopupStoreRes.setReviews(popupReviewResList);

                getPopupStoreResList.add(getPopupStoreRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");

            start = System.currentTimeMillis();
            result = popupStoreRepository.findByStoreAddrWithGoods(storeAddr);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupStoreResList;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }

    public List<GetPopupStoreRes> findByCompanyIdx(Long companyIdx) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupStore>> result = popupStoreRepository.findByCompanyIdx(companyIdx);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupStoreRes> getPopupStoreResList = new ArrayList<>();
            for (PopupStore popupStore : result.get()) {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();
                List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
                for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                    GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                    getPopupGoodsResList.add(getPopupGoodsRes);
                }
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);
                List<GetPopupReviewRes> popupReviewResList = new ArrayList<>();
                for (PopupReview popupReview : popupStore.getReviews()) {
                    GetPopupReviewRes popupReviewRes = GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                    popupReviewResList.add(popupReviewRes);
                }
                getPopupStoreRes.setReviews(popupReviewResList);

                getPopupStoreResList.add(getPopupStoreRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");

            start = System.currentTimeMillis();
            result = popupStoreRepository.findByCompanyIdxWithGoods(companyIdx);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupStoreResList;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }

    public List<GetPopupStoreRes> findByStoreDate(String storeDate) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupStore>> result = popupStoreRepository.findByCategory(storeDate);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupStoreRes> getPopupStoreResList = new ArrayList<>();
            for (PopupStore popupStore : result.get()) {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();
                List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
                for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                    GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                    getPopupGoodsResList.add(getPopupGoodsRes);
                }
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);
                List<GetPopupReviewRes> popupReviewResList = new ArrayList<>();
                for (PopupReview popupReview : popupStore.getReviews()) {
                    GetPopupReviewRes popupReviewRes = GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                    popupReviewResList.add(popupReviewRes);
                }
                getPopupStoreRes.setReviews(popupReviewResList);

                getPopupStoreResList.add(getPopupStoreRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");

            start = System.currentTimeMillis();
            result = popupStoreRepository.findByCategoryWithGoods(storeDate);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupStoreResList;
        } else {
            throw new RuntimeException("No popup stores found");
        }
    }
}