package com.fiiiiive.zippop.cart;

import com.fiiiiive.zippop.cart.model.request.CreateCartReq;
import com.fiiiiive.zippop.cart.model.response.CountCartRes;
import com.fiiiiive.zippop.cart.model.response.CreateCartRes;
import com.fiiiiive.zippop.cart.model.response.GetCartRes;
import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "cart-api", description = "Cart")
@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CustomerRepository customerRepository;

//    @ExeTimer
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateCartReq dto) throws Exception {
        CreateCartRes response = cartService.register(customUserDetails, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.CART_REGISTER_SUCCESS, response));
    }

//    @ExeTimer
    @GetMapping("/adjust-count")
    public ResponseEntity<BaseResponse> adjustCount(@RequestParam Long cartIdx, @RequestParam Long operation) throws BaseException {
        CountCartRes response  = cartService.adjustCount(cartIdx, operation);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.CART_COUNT_SUCCESS, response));
    }

//    @ExeTimer
    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        List<GetCartRes> response = cartService.list(customUserDetails);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.CART_SEARCH_LIST_SUCESS, response));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws BaseException {
        cartService.deleteAll(customUserDetails);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.CART_DELETE_ALL_SUCCESS));
    }

    @DeleteMapping("/delete-cart")
    public ResponseEntity<BaseResponse> deleteByCartIdx(@RequestParam Long cartIdx) throws BaseException {
        cartService.deleteByCartIdx(cartIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.CART_DELETE_SUCCESS));
    }



    // 장바구니에서 결제버튼클릭시 리다이렉션과 함께 결제에게 전송

}