package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.res.PopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.res.PopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.req.PopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.res.PopupStoreRes;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.*;

@Service
public class PopupStoreService {
    private final PopupStoreRepository popupStoreRepository;
    private final CompanyRepository companyRepository;

    public PopupStoreService(PopupStoreRepository popupStoreRepository, CompanyRepository companyRepository) {
        this.popupStoreRepository = popupStoreRepository;
        this.companyRepository = companyRepository;
    }

    public void register(PopupStoreReq popupStoreReq) {
        Company company = companyRepository.findById(popupStoreReq.getCompanyIdx())
                .orElseThrow(() -> new RuntimeException("Company not found"));


        PopupStore popupStore = PopupStore.builder()
                .storeName(popupStoreReq.getStoreName())
                .storeAddr(popupStoreReq.getStoreAddr())
                .storeDate(popupStoreReq.getStoreDate())
                .category(popupStoreReq.getCategory())
                .company(company)
                .build();
        System.out.println(popupStore);
        popupStoreRepository.save(popupStore);
    }

    public Optional<List<PopupStoreRes>> findAll() {
        List<PopupStore> popupStores = popupStoreRepository.findAll();
        if (popupStores.isEmpty()) {
            return Optional.empty();
        }
        List<PopupStoreRes> popupStoreResList = new ArrayList<>();
        for (PopupStore popupStore : popupStores) {
            PopupStoreRes popupStoreRes = new PopupStoreRes();
            popupStoreRes = popupStoreRes.convertToPopupStoreRes(popupStore);
            List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
                popupGoodsRes = popupGoodsRes.convertToPopupGoodsRes(popupGoods);
                popupGoodsResList.add(popupGoodsRes);
            }
            popupStoreRes.setPopupGoodsList(popupGoodsResList);

            List<PopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : popupStore.getReviews()) {
                PopupReviewRes reviewRes = new PopupReviewRes();
                reviewRes = reviewRes.convertToReviewRes(review);
                reviewResList.add(reviewRes);
            }
            popupStoreRes.setReviews(reviewResList);

            popupStoreResList.add(popupStoreRes);
        }

        return Optional.of(popupStoreResList);
    }

//    public PopupStoreRes find_with_goods(String store_name) {
//        PopupStore popupStore = popupStoreRepository.findByStoreName(store_name);
//        PopupStoreRes popupStoreRes = convertToPopupStoreRes(popupStore);
//        List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
//        for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
//            PopupGoodsRes popupGoodsRes = convertToPopupGoodsRes(popupGoods);
//            popupGoodsResList.add(popupGoodsRes);
//        }
//        popupStoreRes.setPopupGoodsList(popupGoodsResList);
//
//        List<PopupReviewRes> reviewResList = new ArrayList<>();
//        for (PopupReview review : popupStore.getReviews()) {
//            PopupReviewRes reviewRes = convertToReviewRes(review);
//            reviewResList.add(reviewRes);
//        }
//        popupStoreRes.setReviews(reviewResList);
//
//        return popupStoreRes;
//    }

    public Optional<List<PopupStoreRes>> findByCategory(String category) {
        List<PopupStore> popupStores = popupStoreRepository.findByCategory(category);
        if (popupStores.isEmpty()) {
            return Optional.empty();
        }
        List<PopupStoreRes> popupStoreResList = new ArrayList<>();
        for (PopupStore popupStore : popupStores) {
            PopupStoreRes popupStoreRes = new PopupStoreRes();
            popupStoreRes = popupStoreRes.convertToPopupStoreRes(popupStore);
            List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
                popupGoodsRes = popupGoodsRes.convertToPopupGoodsRes(popupGoods);
                popupGoodsResList.add(popupGoodsRes);
            }
            popupStoreRes.setPopupGoodsList(popupGoodsResList);

            List<PopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : popupStore.getReviews()) {
                PopupReviewRes reviewRes = new PopupReviewRes();
                reviewRes = reviewRes.convertToReviewRes(review);
                reviewResList.add(reviewRes);
            }
            popupStoreRes.setReviews(reviewResList);

            popupStoreResList.add(popupStoreRes);
        }

        return Optional.of(popupStoreResList);
    }

//    public PopupStoreRes findByStoreName_with_review(String store_name) {
//        PopupStore popupStore = popupStoreRepository.findByStoreName(store_name);
//        PopupStoreRes popupStoreRes = convertToPopupStoreRes(popupStore);
//        return popupStoreRes;
//    }

    public PopupStoreRes findByStoreName(String store_name) {
        PopupStore popupStore = popupStoreRepository.findByStoreName(store_name);
        PopupStoreRes popupStoreRes = new PopupStoreRes();
        popupStoreRes = popupStoreRes.convertToPopupStoreRes(popupStore);
        List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
        for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
            PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
            popupGoodsRes = popupGoodsRes.convertToPopupGoodsRes(popupGoods);
            popupGoodsResList.add(popupGoodsRes);
        }
        popupStoreRes.setPopupGoodsList(popupGoodsResList);

        List<PopupReviewRes> reviewResList = new ArrayList<>();
        for (PopupReview review : popupStore.getReviews()) {
            PopupReviewRes reviewRes = new PopupReviewRes();
            reviewRes = reviewRes.convertToReviewRes(review);
            reviewResList.add(reviewRes);
        }
        popupStoreRes.setReviews(reviewResList);

        return popupStoreRes;
    }

//    public PopupStoreRes findByStoreName_with_goods(String store_name) {
//        PopupStore popupStore = popupStoreRepository.findByStoreName(store_name);
//        PopupStoreRes popupStoreRes = convertToPopupStoreRes(popupStore);
//        return popupStoreRes;
//    }

    public Optional<List<PopupStoreRes>> findByStoreAddr(String store_addr) {
        List<PopupStore> popupStores = popupStoreRepository.findByStoreAddr(store_addr);
        if (popupStores.isEmpty()) {
            return Optional.empty();
        }
        List<PopupStoreRes> popupStoreResList = new ArrayList<>();
        for (PopupStore popupStore : popupStores) {
            PopupStoreRes popupStoreRes = new PopupStoreRes();
            popupStoreRes = popupStoreRes.convertToPopupStoreRes(popupStore);
            List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
                popupGoodsRes = popupGoodsRes.convertToPopupGoodsRes(popupGoods);
                popupGoodsResList.add(popupGoodsRes);
            }
            popupStoreRes.setPopupGoodsList(popupGoodsResList);

            List<PopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : popupStore.getReviews()) {
                PopupReviewRes reviewRes = new PopupReviewRes();
                reviewRes = reviewRes.convertToReviewRes(review);
                reviewResList.add(reviewRes);
            }
            popupStoreRes.setReviews(reviewResList);

            popupStoreResList.add(popupStoreRes);
        }

        return Optional.of(popupStoreResList);
    }

    public Optional<List<PopupStoreRes>> findByCompanyIdx(Long company_idx) {
        List<PopupStore> popupStores = popupStoreRepository.findByCompanyIdx(company_idx);
        if (popupStores.isEmpty()) {
            return Optional.empty();
        }
        List<PopupStoreRes> popupStoreResList = new ArrayList<>();
        for (PopupStore popupStore : popupStores) {
            PopupStoreRes popupStoreRes = new PopupStoreRes();
            popupStoreRes = popupStoreRes.convertToPopupStoreRes(popupStore);
            List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : popupStore.getPopupGoodsList()) {
                PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
                popupGoodsRes = popupGoodsRes.convertToPopupGoodsRes(popupGoods);
                popupGoodsResList.add(popupGoodsRes);
            }
            popupStoreRes.setPopupGoodsList(popupGoodsResList);

            List<PopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : popupStore.getReviews()) {
                PopupReviewRes reviewRes = new PopupReviewRes();
                reviewRes = reviewRes.convertToReviewRes(review);
                reviewResList.add(reviewRes);
            }
            popupStoreRes.setReviews(reviewResList);

            popupStoreResList.add(popupStoreRes);
        }

        return Optional.of(popupStoreResList);
    }

//    public Optional<List<PopupStoreRes>> findByStoreDate(String store_date) {
//        List<PopupStore> popupStores = popupStoreRepository.findByStoreDate(store_date);
//        if (popupStores.isEmpty()) {
//            return Optional.empty();
//        }
//        List<PopupStoreRes> popupStoreResList = new ArrayList<>();
//        for (PopupStore popupStore : popupStores) {
//            PopupStoreRes popupStoreRes = convertToPopupStoreRes(popupStore);
//            for(PopupGoods pg : popupStore.getPopupGoodsList())
//                popupStoreRes.getPopupGoodsList().add(convertToPopupGoodsRes(pg));
//            popupStoreResList.add(popupStoreRes);
//        }
//
//        return Optional.of(popupStoreResList);
//    }

//    private PopupStoreRes convertToPopupStoreRes(PopupStore popupStore) {
//        PopupStoreRes popupStoreRes = PopupStoreRes.builder()
//                .storeName(popupStore.getStoreName())
//                .storeAddr(popupStore.getStoreAddr())
//                .storeDate(popupStore.getStoreDate())
//                .category(popupStore.getCategory())
//                .build();
//        return popupStoreRes;
//    }
//
//    private PopupGoodsRes convertToPopupGoodsRes(PopupGoods popupGoods) {
//        PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
//        popupGoodsRes.setProductIdx(popupGoods.getProductIdx());
//        popupGoodsRes.setProductName(popupGoods.getProductName());
//        popupGoodsRes.setProductPrice(popupGoods.getProductPrice());
//        popupGoodsRes.setProductContent(popupGoods.getProductContent());
//        popupGoodsRes.setProductImg(popupGoods.getProductImg());
//        popupGoodsRes.setProductAmount(popupGoods.getProductAmount());
//        return popupGoodsRes;
//    }
//
//    private PopupReviewRes convertToReviewRes(PopupReview review) {
//        PopupReviewRes popupReviewRes = new PopupReviewRes();
//        popupReviewRes.setReviewTitle(review.getReviewTitle());
//        popupReviewRes.setReviewContent(review.getReviewContent());
//        popupReviewRes.setRating(review.getRating());
//        popupReviewRes.setReviewDate(review.getReviewDate());
//        popupReviewRes.setStoreName(review.getStoreName());
//        return popupReviewRes;
//
//    }
}
