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
        Optional<Favorite> result = favoriteRepository.findByCustomerIdxAndStoreIdx(customUserDetails.getIdx(), storeIdx);
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
        List<Favorite> favoriteList = favoriteRepository.findAllByCustomerIdx(customUserDetails.getIdx())
        .orElseThrow(()->new BaseException(BaseResponseMessage.FAVORITE_SEARCH_ALL_FAIL));
        List<GetFavoriteRes> getFavoriteResList = new ArrayList<>();
        for(Favorite favorite: favoriteList){

            GetFavoriteRes getFavoriteRes = GetFavoriteRes.builder()
                    .popupStore(favorite.getPopupStore())
                    .build();
            getFavoriteResList.add(getFavoriteRes);
        }
        return getFavoriteResList;
    }
}
