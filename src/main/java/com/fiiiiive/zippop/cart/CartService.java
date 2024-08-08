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
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsImageRes;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final CustomerRepository customerRepository;

    public CreateCartRes register(CustomUserDetails customUserDetails, CreateCartReq dto) throws BaseException {
        Customer customer = customerRepository.findByCustomerEmail(customUserDetails.getEmail())
                .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_MEMBER_NOT_FOUND)));
        PopupGoods popupGoods = popupGoodsRepository.findById(dto.getProductIdx())
                .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_GOODS_NOT_FOUND)));
        Optional<Cart> result = cartRepository.findByCustomerEmailAndProductIdx(customUserDetails.getEmail(), dto.getProductIdx());
        if(result.isPresent()){ throw new BaseException(BaseResponseMessage.CART_REGISTER_FAIL_EXIST); }
        Cart cart = Cart.builder()
                .customer(customer)
                .popupGoods(popupGoods)
                .itemCount(dto.getItemCount())
                .itemPrice(popupGoods.getProductPrice())
                .build();
        cartRepository.save(cart);
        return CreateCartRes.builder()
                .cartIdx(cart.getCartIdx())
                .customerIdx(customer.getCustomerIdx())
                .productIdx(popupGoods.getProductIdx())
                .itemCount(cart.getItemCount())
                .itemPrice(cart.getItemPrice())
                .build();
    }


    public List<GetCartRes> searchAll(CustomUserDetails customUserDetails) throws BaseException {
        List<Cart> cartList = cartRepository.findAllByCustomerEmail(customUserDetails.getEmail())
                .orElseThrow(() -> new BaseException(BaseResponseMessage.CART_SEARCH_FAIL));
        List<GetCartRes> getCartResList = new ArrayList<>();
        for(Cart cart: cartList){
            PopupGoods popupGoods = cart.getPopupGoods();
            List<GetPopupGoodsImageRes> imageResList = popupGoods.getPopupGoodsImageList().stream()
                    .map(image -> GetPopupGoodsImageRes.builder()
                            .productImageIdx(image.getPopupGoodsImageIdx())
                            .imageUrl(image.getImageUrl())
                            .createdAt(image.getCreatedAt())
                            .updatedAt(image.getUpdatedAt())
                            .build())
                    .collect(Collectors.toList());

            GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                    .productIdx(popupGoods.getProductIdx())
                    .productName(popupGoods.getProductName())
                    .productPrice(popupGoods.getProductPrice())
                    .productContent(popupGoods.getProductContent())
                    .productAmount(popupGoods.getProductAmount())
                    .createdAt(popupGoods.getCreatedAt())
                    .updatedAt(popupGoods.getUpdatedAt())
                    .getPopupGoodsImageResList(imageResList)
                    .build();

            GetCartRes getCartRes = GetCartRes.builder()
                    .cartIdx(cart.getCartIdx())
                    .getPopupGoodsRes(getPopupGoodsRes)
                    .itemCount(cart.getItemCount())
                    .itemPrice(cart.getItemPrice())

                    .build();
            getCartResList.add(getCartRes);
        }
        return getCartResList;
    }

    public CountCartRes count(CustomUserDetails customUserDetails, Long cartIdx, Long operation) throws BaseException {
        Cart cart = cartRepository.findByIdAndCustomerEmail(cartIdx, customUserDetails.getEmail())
                .orElseThrow(() -> (new BaseException(BaseResponseMessage.CART_COUNT_FAIL_NOT_FOUND)));
        if (operation == 0){
            cart.setItemCount(cart.getItemCount() + 1);
            cartRepository.save(cart);
            return CountCartRes.builder().cart(cart).build();
        } else if (operation == 1) {
            if(cart.getItemCount() == 0){
                throw new BaseException(BaseResponseMessage.CART_COUNT_FAIL_IS_0);
            }
            cart.setItemCount(cart.getItemCount() - 1);
            cartRepository.save(cart);
            return CountCartRes.builder().cart(cart).build();
        } else{
            throw new BaseException(BaseResponseMessage.CART_COUNT_FAIL_INVALID_OPERATION);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(CustomUserDetails customUserDetails, Long cartIdx) throws BaseException {
        cartRepository.deleteByIdAndCustomerEmail(cartIdx, customUserDetails.getEmail());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(CustomUserDetails customUserDetails) throws BaseException {
        cartRepository.deleteAllByCustomerEmail(customUserDetails.getEmail());
    }
}


