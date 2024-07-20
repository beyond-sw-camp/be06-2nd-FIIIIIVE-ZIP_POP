package com.fiiiiive.zippop.popup_goods;


import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> register(@RequestBody CreatePopupGoodsReq createPopupGoodsReq) {
        popupGoodsService.register(createPopupGoodsReq);
        return ResponseEntity.ok("등록 성공");
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetPopupGoodsRes>> search() {
        List<GetPopupGoodsRes> popupGoodsList = popupGoodsService.findAll();
        return ResponseEntity.ok(popupGoodsList);
    }

    @GetMapping("/search-product-name")
    public ResponseEntity<List<GetPopupGoodsRes>> search_product_name(@RequestParam String productName) {
        List<GetPopupGoodsRes> popupGoodsList = popupGoodsService.findByProductName(productName);
        return ResponseEntity.ok(popupGoodsList);
    }

    @GetMapping("/search-store-name")
    public ResponseEntity<List<GetPopupGoodsRes>> search_store_name(@RequestParam String storeName) {
        List<GetPopupGoodsRes> popupGoods = popupGoodsService.findByStoreName(storeName);
        return ResponseEntity.ok(popupGoods);
    }

    @GetMapping("/search-product-price")
    public ResponseEntity<List<GetPopupGoodsRes>> search_product_price(@RequestParam Integer productPrice) {
        List<GetPopupGoodsRes> popupGoodsList = popupGoodsService.findByProductPrice(productPrice);
        return ResponseEntity.ok(popupGoodsList);
    }
}
