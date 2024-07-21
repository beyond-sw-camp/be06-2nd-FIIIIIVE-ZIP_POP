package com.fiiiiive.zippop.popup_goods;


import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Tag(name = "popup-goods-api", description = "PopupGoods")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-goods")
public class PopupGoodsController {
    private final PopupGoodsService popupGoodsService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody CreatePopupGoodsReq createPopupGoodsReq) throws Exception {
        popupGoodsService.register(createPopupGoodsReq);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_REGISTER_SUCCESS));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> search(@RequestParam Pageable pageable) throws Exception{
        Page<GetPopupGoodsRes> popupGoodsPage = popupGoodsService.findAll(pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoodsPage));
    }
    @GetMapping("/search-product-name")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> search_product_name(@RequestParam String productName, @RequestParam Pageable pageable) throws Exception{
        Page<GetPopupGoodsRes> popupGoodsPage = popupGoodsService.findByProductName(productName, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoodsPage));
    }

    @GetMapping("/search-store-name")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> search_store_name(@RequestParam String storeName, @RequestParam Pageable pageable) throws Exception {
        Page<GetPopupGoodsRes> popupGoods = popupGoodsService.findByStoreName(storeName, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoods));
    }

    @GetMapping("/search-product-price")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> search_product_price(@RequestParam Integer productPrice, @RequestParam Pageable pageable) throws Exception{
        Page<GetPopupGoodsRes> popupGoodsPage = popupGoodsService.findByProductPrice(productPrice, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoodsPage));
    }
}
