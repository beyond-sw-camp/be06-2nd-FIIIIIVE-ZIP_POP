package com.fiiiiive.zippop.favorite;

import com.fiiiiive.zippop.cart.model.Cart;
import com.fiiiiive.zippop.cart.model.response.GetCartRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.favorite.model.Favorite;
import com.fiiiiive.zippop.favorite.model.response.GetFavoriteRes;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreImageRes;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final CustomerRepository customerRepository;

    public void active(CustomUserDetails customUserDetails, Long storeIdx) throws BaseException {
        Customer customer = customerRepository.findById(customUserDetails.getIdx())
        .orElseThrow(() -> (new BaseException(BaseResponseMessage.FAVORITE_ACTIVE_FAIL_MEMBER_NOT_FOUND)));
        PopupStore popupStore = popupStoreRepository.findById(storeIdx)
        .orElseThrow(() -> (new BaseException(BaseResponseMessage.FAVORITE_ACTIVE_FAIL_STORE_NOT_FOUND)));
        Optional<Favorite> result = favoriteRepository.findByCustomerEmailAndStoreIdx(customUserDetails.getEmail(), storeIdx);
        if(result.isPresent()){
            Favorite favorite = result.get();
            favoriteRepository.deleteById(favorite.getFavoriteIdx());
        } else {
            Favorite favorite = Favorite.builder()
                    .popupStore(popupStore)
                    .customer(customer)
                    .build();
            favoriteRepository.save(favorite);
        }
    }

    public List<GetFavoriteRes> list(CustomUserDetails customUserDetails) throws BaseException {
        List<Favorite> favoriteList = favoriteRepository.findAllByCustomerEmail(customUserDetails.getEmail())
        .orElseThrow(()->new BaseException(BaseResponseMessage.FAVORITE_SEARCH_ALL_FAIL));
        List<GetFavoriteRes> getFavoriteResList = new ArrayList<>();
        for(Favorite favorite: favoriteList){

            PopupStore popupStore = favorite.getPopupStore();
            List<PopupStoreImage> popupStoreImageList = popupStore.getPopupstoreImageList();
            List<GetPopupStoreImageRes> getPopupStoreImageResList = new ArrayList<>();
            for(PopupStoreImage popupStoreImage : popupStoreImageList ){
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
                    .createdAt(popupStore.getCreatedAt())
                    .updatedAt(popupStore.getUpdatedAt())
                    .getPopupStoreImageResList(getPopupStoreImageResList)
                    .build();
            GetFavoriteRes getFavoriteRes = GetFavoriteRes.builder()
                    .getPopupStoreRes(getPopupStoreRes)
                    .build();
            getFavoriteResList.add(getFavoriteRes);
        }
        return getFavoriteResList;
    }
}
