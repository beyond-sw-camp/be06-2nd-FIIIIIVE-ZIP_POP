package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.req.PopupStoreReq;
import com.fiiiiive.zippop.popup_store.res.PopupStoreRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/popup_store")
public class PopupStoreController {
    private final PopupStoreService popupStoreService;

    public PopupStoreController(PopupStoreService popupStoreService) {
        this.popupStoreService = popupStoreService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PopupStoreReq popupStoreReq) {
        popupStoreService.register(popupStoreReq);
        return ResponseEntity.ok("등록성공");
    }

    @GetMapping("/search")
    public ResponseEntity<Optional<List<PopupStoreRes>>> search() {
        Optional<List<PopupStoreRes>> popupStoreList = popupStoreService.findAll();
        return ResponseEntity.ok(popupStoreList);
    }

//    @GetMapping("/search_with_goods")
//    public ResponseEntity<PopupStoreRes> search_with_goods(@RequestParam String store_name) {
//        PopupStoreRes popupStoreRes = popupStoreService.find_with_goods(store_name);
//
//        return ResponseEntity.ok(popupStoreRes);
//    }

//    @GetMapping("/search_with_reviews")
//    public ResponseEntity<PopupStoreRes> search_with_reviews(@RequestParam String store_name) {
//        PopupStoreRes popupStoreRes = popupStoreService.findByStoreName_with_review(store_name);
//        return ResponseEntity.ok(popupStoreRes);
//    }

    @GetMapping("/search_category")
    public ResponseEntity<Optional<List<PopupStoreRes>>> search_category(@RequestParam String category) {
        Optional<List<PopupStoreRes>> popupStoreResList = popupStoreService.findByCategory(category);
        return ResponseEntity.ok(popupStoreResList);
    }

    @GetMapping("/search_store_name")
    public ResponseEntity<PopupStoreRes> search_store_name(@RequestParam String store_name) {
        PopupStoreRes popupStoreRes = popupStoreService.findByStoreName(store_name);
        return ResponseEntity.ok(popupStoreRes);
    }

    @GetMapping("/search_store_addr")
    public ResponseEntity<Optional<List<PopupStoreRes>>> search_store_addr(@RequestParam String store_addr) {
        Optional<List<PopupStoreRes>> popupStoreResList = popupStoreService.findByStoreAddr(store_addr);
        return ResponseEntity.ok(popupStoreResList);
    }

//    @GetMapping("/search_store_date")
//    public ResponseEntity<Optional<List<PopupStoreRes>>> search_store_date(@RequestParam String store_date) {
//        Optional<List<PopupStoreRes>> popupStoreResList = popupStoreService.findByStoreDate(store_date);
//        return ResponseEntity.ok(popupStoreResList);
//    }
}
