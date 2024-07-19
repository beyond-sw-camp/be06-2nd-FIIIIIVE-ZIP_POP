package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopupGoodsService {
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;

    public void register(CreatePopupGoodsReq createPopupGoodsReq) {
        PopupGoods goods = PopupGoods.builder()
                .productName(createPopupGoodsReq.getProductName())
                .productAmount(createPopupGoodsReq.getProductAmount())
                .productPrice(createPopupGoodsReq.getProductPrice())
                .storeName(createPopupGoodsReq.getStoreName())
                .build();
        Optional<PopupStore> optionalStore = popupStoreRepository.findByStoreName(createPopupGoodsReq.getStoreName());
        if (optionalStore.isPresent()) {
            // PopupGoods 설정
            goods.setPopupStore(optionalStore.get());
            // PopupGoods 저장
            popupGoodsRepository.save(goods);

            // PopupStore의 PopupGoods 리스트에 추가
            optionalStore.get().getPopupGoodsList().add(goods);
        } else {
            throw new RuntimeException("Store not found");
        }
    }


    public List<GetPopupGoodsRes> findAll() {
        Long start = System.currentTimeMillis();
        Optional<List<PopupGoods>> result = Optional.of(popupGoodsRepository.findAll());
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                        .storeName(popupGoods.getPopupStore().getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = Optional.of(popupGoodsRepository.findAllWithStore());
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupGoodsResList;
        } else {
            throw new RuntimeException("No popup goods found");
        }

    }

    public List<GetPopupGoodsRes> findByProductName(String productName) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupGoods>> result = popupGoodsRepository.findByProductName(productName);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                        .storeName(popupGoods.getPopupStore().getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = popupGoodsRepository.findByProductNameWithStore(productName);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupGoodsResList;
        } else {
            throw new RuntimeException("No popup goods found");
        }

    }

    public List<GetPopupGoodsRes> findByStoreName(String storeName) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupGoods>> result = popupGoodsRepository.findByStoreName(storeName);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                        .storeName(popupGoods.getPopupStore().getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = popupGoodsRepository.findByStoreNameWithStore(storeName);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupGoodsResList;
        } else {
            throw new RuntimeException("No popup goods found");
        }
    }

    public List<GetPopupGoodsRes> findByProductPrice(Integer productPrice) {
        Long start = System.currentTimeMillis();
        Optional<List<PopupGoods>> result = popupGoodsRepository.findByProductPrice(productPrice);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                        .storeName(popupGoods.getPopupStore().getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = popupGoodsRepository.findByProductPriceWithStore(productPrice);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPopupGoodsResList;
        } else {
            throw new RuntimeException("No popup goods found");
        }
    }

}
