package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopupGoodsService {
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;

    public void register(CreatePopupGoodsReq createPopupGoodsReq) throws BaseException {
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
            throw new BaseException(BaseResponseMessage.POPUP_GOODS_REGISTER_FAIL);
        }
    }

    public Page<GetPopupGoodsRes> findAll(Pageable pageable) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findAll(pageable); //
        if (result.hasContent()) {
            Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods -> GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productImg(popupGoods.getProductImg())
                    .productAmount(popupGoods.getProductAmount())
                    .storeName(popupGoods.getStoreName())
                    .build());
            result = popupGoodsRepository.findAllFetchJoin(pageable);
            return getPopupGoodsResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REVIEW_FAIL_STORE_NOT_EXIST);
        }
    }

    public Page<GetPopupGoodsRes> findByProductName(String productName, Pageable pageable) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findByProductName(productName, pageable);
        if (result.hasContent()) {
            Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods -> GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productImg(popupGoods.getProductImg())
                    .productAmount(popupGoods.getProductAmount())
                    .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                    .storeName(popupGoods.getPopupStore().getStoreName())
                    .build());
            result = popupGoodsRepository.findByProductNameFetchJoin(productName, pageable);
            return getPopupGoodsResPage;

        } else {
            throw new BaseException(BaseResponseMessage.POPUP_GOODS_SEARCH_FAIL);
        }
    }

    public Page<GetPopupGoodsRes> findByStoreName(String storeName, Pageable pageable) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findByStoreName(storeName, pageable);
        if (result.hasContent()) {
            Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods -> GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productImg(popupGoods.getProductImg())
                    .productAmount(popupGoods.getProductAmount())
                    .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                    .storeName(popupGoods.getPopupStore().getStoreName())
                    .build());
            result = popupGoodsRepository.findByStoreNameFetchJoin(storeName, pageable);
            return getPopupGoodsResPage;

        } else {
            throw new BaseException(BaseResponseMessage.POPUP_GOODS_SEARCH_FAIL);
        }
    }

    public Page<GetPopupGoodsRes> findByProductPrice(Integer productPrice, Pageable pageable) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findByProductPrice(productPrice, pageable);

        if (result.hasContent()) {
            Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods -> GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productImg(popupGoods.getProductImg())
                    .productAmount(popupGoods.getProductAmount())
                    .storeIdx(popupGoods.getPopupStore().getStoreIdx())
                    .storeName(popupGoods.getPopupStore().getStoreName())
                    .build());
            result = popupGoodsRepository.findByProductPriceFetchJoin(productPrice, pageable);

            return getPopupGoodsResPage;

        } else {
            throw new BaseException(BaseResponseMessage.POPUP_GOODS_SEARCH_FAIL);
        }
    }


}
