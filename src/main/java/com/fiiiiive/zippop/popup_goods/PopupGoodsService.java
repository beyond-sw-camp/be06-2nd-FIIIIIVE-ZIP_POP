package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.popup_goods.req.PopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.res.PopupGoodsRes;
import com.fiiiiive.zippop.popup_store.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PopupGoodsService {
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;

    public PopupGoodsService(PopupGoodsRepository popupGoodsRepository, PopupStoreRepository popupStoreRepository) {
        this.popupGoodsRepository = popupGoodsRepository;
        this.popupStoreRepository = popupStoreRepository;

    }

    public void register(PopupGoodsReq popupGoodsReq) {
        PopupGoods goods = PopupGoods.builder()
                .productName(popupGoodsReq.getProductName())
                .productAmount(popupGoodsReq.getProductAmount())
                .productPrice(popupGoodsReq.getProductPrice())
                .storeName(popupGoodsReq.getStoreName())
                .build();
        PopupStore optionalStore = popupStoreRepository.findByStoreName(popupGoodsReq.getStoreName());
        if (optionalStore != null) {
            // PopupGoods 설정
            goods.setPopupStore(optionalStore);
            // PopupGoods 저장
            popupGoodsRepository.save(goods);

            // PopupStore의 PopupGoods 리스트에 추가
            optionalStore.getPopupGoodsList().add(goods);
        } else {
            throw new RuntimeException("Store not found");
        }
    }


    public Optional<List<PopupGoodsRes>> findAll() {
        List<PopupGoods> popupGoodsList = popupGoodsRepository.findAll();
        if (popupGoodsList.isEmpty()) {
            return Optional.empty();
        }
        List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
        for (PopupGoods popupGoods : popupGoodsList) {
            PopupGoodsRes popupGoodsRes = convertToPopupGoodsRes(popupGoods);
            popupGoodsResList.add(popupGoodsRes);
        }

        return Optional.of(popupGoodsResList);
    }

    public Optional<List<PopupGoodsRes>> findByProductName(String product_name) {
        List<PopupGoods> popupGoodsList = popupGoodsRepository.findByProductName(product_name);
        if (popupGoodsList.isEmpty()) {
            return Optional.empty();
        }
        List<PopupGoodsRes> popupGoodsResList = new ArrayList<>();
        for (PopupGoods popupGoods : popupGoodsList) {
            PopupGoodsRes popupGoodsRes = convertToPopupGoodsRes(popupGoods);
            popupGoodsResList.add(popupGoodsRes);
        }

        return Optional.of(popupGoodsResList);
    }

    public Optional<List<PopupGoods>> findByStoreName(String store_name) {
        return popupGoodsRepository.findByStoreName(store_name);
    }

    public Optional<List<PopupGoods>> findByProductPrice(Integer product_price) {
        List<PopupGoods> popupGoods = popupGoodsRepository.findByProductPrice(product_price);
        return Optional.of(popupGoods);
    }

    private PopupGoodsRes convertToPopupGoodsRes(PopupGoods popupGoods) {
        PopupGoodsRes popupGoodsRes = new PopupGoodsRes();
        popupGoodsRes.setProductIdx(popupGoods.getProductIdx());
        popupGoodsRes.setProductName(popupGoods.getProductName());
        popupGoodsRes.setProductPrice(popupGoods.getProductPrice());
        popupGoodsRes.setProductContent(popupGoods.getProductContent());
        popupGoodsRes.setProductImg(popupGoods.getProductImg());
        popupGoodsRes.setProductAmount(popupGoods.getProductAmount());
        return popupGoodsRes;
    }
}
