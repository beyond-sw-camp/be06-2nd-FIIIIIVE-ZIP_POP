package com.fiiiiive.zippop.cart;

import com.fiiiiive.zippop.cart.model.Cart;
import com.fiiiiive.zippop.cart.model.request.CreateCartReq;
import com.fiiiiive.zippop.cart.model.response.CountCartRes;
import com.fiiiiive.zippop.cart.model.response.CreateCartRes;
import com.fiiiiive.zippop.cart.model.response.GetCartRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_goods.PopupGoodsRepository;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final CustomerRepository customerRepository;

    public CreateCartRes register(CustomUserDetails customUserDetails, CreateCartReq dto) throws BaseException {
            Customer customer = customerRepository.findById(customUserDetails.getIdx())
                    .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_MEMBER_NOT_FOUND)));
            PopupGoods popupGoods = popupGoodsRepository.findById(dto.getProductIdx())
                    .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_GOODS_NOT_FOUND)));
            Optional<Cart> result = cartRepository.findByCustomerIdxAndProductIdx(customUserDetails.getIdx(), dto.getProductIdx());
            if(result.isPresent()){
                throw new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_EXIST);
            } else {
                Cart cart = Cart.builder()
                        .customer(customer)
                        .popupGoods(popupGoods)
                        .cartItemCount(dto.getCartItemCount())
                        .cartItemPrice(popupGoods.getProductPrice())
                        .build();
                cartRepository.save(cart);
                return CreateCartRes.builder()
                        .cartIdx(cart.getCartIdx())
                        .customerIdx(customer.getCustomerIdx())
                        .productIdx(popupGoods.getProductIdx())
                        .cartItemCount(cart.getCartItemCount())
                        .cartItemPrice(cart.getCartItemPrice())
                        .build();
            }
    }

    public CountCartRes count(CustomUserDetails customUserDetails, Long cartIdx, Long operation) throws BaseException {
        Cart cart = cartRepository.findByIdAndCustomerIdx(cartIdx, customUserDetails.getIdx())
                .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_COUNT_FAIL_NOT_FOUND)));
        if (operation == 0){
            cart.setCartItemCount(cart.getCartItemCount() + 1);
            cartRepository.save(cart);
            return CountCartRes.builder().cart(cart).build();
        } else if (operation == 1) {
            if(cart.getCartItemCount() == 0){
                throw new BaseException(BaseResponseMessage.CART_COUNT_FAIL_IS_0);
            }
            cart.setCartItemCount(cart.getCartItemCount() - 1);
            cartRepository.save(cart);
            return CountCartRes.builder().cart(cart).build();
        } else{
            throw new BaseException(BaseResponseMessage.CART_COUNT_FAIL_INVALID_OPERATION);
        }
    }

    public List<GetCartRes> searchAll(CustomUserDetails customUserDetails) throws BaseException {
        Optional<List<Cart>> result = cartRepository.findAllByCustomerIdx(customUserDetails.getIdx());
        if (result.isPresent()) {
            List<GetCartRes> getCartResList = new ArrayList<>();
            for(Cart cart: result.get()){
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(cart.getPopupGoods().getProductIdx())
                        .productName(cart.getPopupGoods().getProductName())
                        .productPrice(cart.getPopupGoods().getProductPrice())
                        .productContent(cart.getPopupGoods().getProductContent())
                        .productImg(cart.getPopupGoods().getProductImg())
                        .productAmount(cart.getPopupGoods().getProductAmount())
                        .storeName(cart.getPopupGoods().getStoreName())
                        .build();
                GetCartRes getCartRes = GetCartRes.builder()
                        .cartIdx(cart.getCartIdx())
                        .getPopupGoodsRes(getPopupGoodsRes)
                        .cartItemCount(cart.getCartItemCount())
                        .cartItemPrice(cart.getCartItemPrice())
                        .build();
                getCartResList.add(getCartRes);
            }
            return getCartResList;
        }
        else {
            throw new BaseException(BaseResponseMessage.CART_SEARCH_FAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(CustomUserDetails customUserDetails, Long cartIdx) throws BaseException {
        int deletedCount = cartRepository.deleteByIdAndCustomerIdx(cartIdx, customUserDetails.getIdx());
        if (deletedCount == 0) {
            throw new BaseException(BaseResponseMessage.CART_DELETE_FAIL_ITEM_NOT_FOUND);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(CustomUserDetails customUserDetails) throws BaseException {
        cartRepository.deleteAllByCustomerIdx(customUserDetails.getIdx());
    }
}


