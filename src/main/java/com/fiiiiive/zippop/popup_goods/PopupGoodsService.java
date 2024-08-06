package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.PopupGoodsImage;
import com.fiiiiive.zippop.popup_goods.model.PopupGoodsImageRepository;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.request.UpdatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.CreatePopupGoodsRes;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsImageRes;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_goods.model.response.UpdatePopupGoodsRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopupGoodsService {
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final PopupGoodsImageRepository popupGoodsImageRepository;

    public CreatePopupGoodsRes register(CustomUserDetails customUserDetails, Long storeIdx, List<String> fileNames, CreatePopupGoodsReq dto) throws BaseException {
        PopupStore popupStore = popupStoreRepository.findById(storeIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_GOODS_REGISTER_FAIL_STORE_NOT_FOUND));
        if(!Objects.equals(popupStore.getCompanyEmail(), customUserDetails.getEmail())) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_UPDATE_FAIL_INVALID_MEMBER);
        }
        PopupGoods popupGoods = PopupGoods.builder()
                .productName(dto.getProductName())
                .productAmount(dto.getProductAmount())
                .productPrice(dto.getProductPrice())
                .productContent(dto.getProductContent())
                .popupStore(popupStore)
                .build();
        popupGoodsRepository.save(popupGoods);
        List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
        for(String fileName: fileNames){
            PopupGoodsImage popupGoodsImage = PopupGoodsImage.builder()
                    .imageUrl(fileName)
                    .popupGoods(popupGoods)
                    .build();
            popupGoodsImageRepository.save(popupGoodsImage);
            GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                    .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                    .imageUrl(popupGoodsImage.getImageUrl())
                    .createdAt(popupGoodsImage.getCreatedAt())
                    .updatedAt(popupGoodsImage.getUpdatedAt())
                    .build();
            getPopupGoodsImageResList.add(getPopupGoodsImageRes);
        }
        return CreatePopupGoodsRes.builder()
                .productIdx(popupGoods.getProductIdx())
                .productName(popupGoods.getProductName())
                .productPrice(popupGoods.getProductPrice())
                .productContent(popupGoods.getProductContent())
                .productAmount(popupGoods.getProductAmount())
                .popupGoodsImageList(getPopupGoodsImageResList)
                .createdAt(popupGoods.getCreatedAt())
                .updatedAt(popupGoods.getUpdatedAt())
                .build();
    }

    public Page<GetPopupGoodsRes> search(String productName, int page, int size) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findByProductName(productName, PageRequest.of(page, size))
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_GOODS_SEARCH_FAIL_STORE_NOT_NOT_FOUND));
        Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods ->{
            List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
            List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
            for(PopupGoodsImage popupGoodsImage : popupGoodsImageList){
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
                    .getPopupGoodsImageResList(getPopupGoodsImageResList)
                    .build();
            return getPopupGoodsRes;
        });
        return getPopupGoodsResPage;
    }

    public Page<GetPopupGoodsRes> searchStore(Long storeIdx, int page, int size) throws BaseException {
        Page<PopupGoods> result = popupGoodsRepository.findByStoreIdx(storeIdx, PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_GOODS_SEARCH_FAIL_STORE_NOT_NOT_FOUND));
        Page<GetPopupGoodsRes> getPopupGoodsResPage = result.map(popupGoods ->{
            List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
            List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
            for(PopupGoodsImage popupGoodsImage : popupGoodsImageList){
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
                    .getPopupGoodsImageResList(getPopupGoodsImageResList)
                    .build();
            return getPopupGoodsRes;
        });
        return getPopupGoodsResPage;
    }

    public UpdatePopupGoodsRes update(CustomUserDetails customUserDetails, Long productIdx, List<String> fileNames, UpdatePopupGoodsReq dto) throws BaseException{
        PopupGoods popupGoods = popupGoodsRepository.findByProductIdx(productIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_GOODS_REGISTER_FAIL_STORE_NOT_FOUND));
        if(!Objects.equals(popupGoods.getPopupStore().getCompanyEmail(), customUserDetails.getEmail())) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_UPDATE_FAIL_INVALID_MEMBER);
        }
        popupGoods.setProductName(dto.getProductName());
        popupGoods.setProductAmount(dto.getProductAmount());
        popupGoods.setProductPrice(dto.getProductPrice());
        popupGoods.setProductContent(dto.getProductContent());
        popupGoodsRepository.save(popupGoods);
        List<GetPopupGoodsImageRes> getPopupGoodsImageResList = new ArrayList<>();
        List<PopupGoodsImage> popupGoodsImageList = popupGoods.getPopupGoodsImageList();
        for(PopupGoodsImage popupGoodsImage : popupGoodsImageList){
            popupGoodsImageRepository.deleteById(popupGoodsImage.getPopupGoodsImageIdx());
        }
        for(String fileName: fileNames){
            PopupGoodsImage popupGoodsImage = PopupGoodsImage.builder()
                    .imageUrl(fileName)
                    .popupGoods(popupGoods)
                    .build();
            GetPopupGoodsImageRes getPopupGoodsImageRes = GetPopupGoodsImageRes.builder()
                    .productImageIdx(popupGoodsImage.getPopupGoodsImageIdx())
                    .imageUrl(popupGoodsImage.getImageUrl())
                    .createdAt(popupGoodsImage.getCreatedAt())
                    .updatedAt(popupGoodsImage.getUpdatedAt())
                    .build();
            getPopupGoodsImageResList.add(getPopupGoodsImageRes);
        }
        return UpdatePopupGoodsRes.builder()
                .productIdx(popupGoods.getProductIdx())
                .productName(popupGoods.getProductName())
                .productPrice(popupGoods.getProductPrice())
                .productContent(popupGoods.getProductContent())
                .productAmount(popupGoods.getProductAmount())
                .createdAt(popupGoods.getCreatedAt())
                .updatedAt(popupGoods.getUpdatedAt())
                .popupGoodsImageList(getPopupGoodsImageResList)
                .build();
    }

    public void delete(CustomUserDetails customUserDetails, Long productIdx) throws BaseException{
        PopupGoods popupGoods = popupGoodsRepository.findByProductIdx(productIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POPUP_STORE_DELETE_FAIL_NOT_FOUND));
        if(!Objects.equals(popupGoods.getPopupStore().getCompanyEmail(), customUserDetails.getEmail())){
            throw new BaseException(BaseResponseMessage.POPUP_STORE_DELETE_FAIL_INVALID_MEMBER);
        }
        popupGoodsRepository.deleteById(productIdx);
    }

}
