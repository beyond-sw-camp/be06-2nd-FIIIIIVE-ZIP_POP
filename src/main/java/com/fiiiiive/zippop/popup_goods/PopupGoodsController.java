package com.fiiiiive.zippop.popup_goods;


import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.PopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.PopupGoodsRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/popup_goods")
public class PopupGoodsController {
    private final PopupGoodsService popupGoodsService;
    public PopupGoodsController(PopupGoodsService popupGoodsService) {
        this.popupGoodsService = popupGoodsService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PopupGoodsReq popupGoodsReq) {
        popupGoodsService.register(popupGoodsReq);
        return ResponseEntity.ok("등록 성공");
    }


    @GetMapping("/search")
    public ResponseEntity<Optional<List<PopupGoodsRes>>> search() {
        Optional<List<PopupGoodsRes>> popupGoodsList = popupGoodsService.findAll();
        return ResponseEntity.ok(popupGoodsList);
    }

    @GetMapping("/search_product_name")
    public ResponseEntity<Optional<List<PopupGoodsRes>>> search_product_name(@RequestParam String product_name) {
        Optional<List<PopupGoodsRes>> popupGoodsList = popupGoodsService.findByProductName(product_name);
        return ResponseEntity.ok(popupGoodsList);
    }

    @GetMapping("/search_store_name")
    public ResponseEntity<Optional<List<PopupGoods>>> search_store_name(@RequestParam String store_name) {
        Optional<List<PopupGoods>> popupGoods = popupGoodsService.findByStoreName(store_name);
        return ResponseEntity.ok(popupGoods);
    }

    @GetMapping("/search_product_price")
    public ResponseEntity<Optional<List<PopupGoods>>> search_product_price(@RequestParam Integer product_price) {
        Optional<List<PopupGoods>> popupGoodsList = popupGoodsService.findByProductPrice(product_price);
        return ResponseEntity.ok(popupGoodsList);
    }



}
