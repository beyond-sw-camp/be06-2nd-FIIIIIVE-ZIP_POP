package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "popup-store-api", description = "PopupStore")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-store")
public class PopupStoreController {
    private final PopupStoreService popupStoreService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreatePopupStoreReq createPopupStoreReq) {
        popupStoreService.register(customUserDetails, createPopupStoreReq);
        return ResponseEntity.ok("등록성공");
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetPopupStoreRes>> search() {
        List<GetPopupStoreRes> popupStoreList = popupStoreService.findAll();
        return ResponseEntity.ok(popupStoreList);
    }

    @GetMapping("/search-category")
    public ResponseEntity<List<GetPopupStoreRes>> search_category(@RequestParam String category) {
        List<GetPopupStoreRes> popupStoreResList = popupStoreService.findByCategory(category);
        return ResponseEntity.ok(popupStoreResList);
    }

    @GetMapping("/search-store-name")
    public ResponseEntity<GetPopupStoreRes> search_store_name(@RequestParam String storeName) {
        GetPopupStoreRes getPopupStoreRes = popupStoreService.findByStoreName(storeName);
        return ResponseEntity.ok(getPopupStoreRes);
    }

    @GetMapping("/search-store-addr")
    public ResponseEntity<List<GetPopupStoreRes>> search_store_addr(@RequestParam String storeAddr) {
        List<GetPopupStoreRes> popupStoreResList = popupStoreService.findByStoreAddr(storeAddr);
        return ResponseEntity.ok(popupStoreResList);
    }

    @GetMapping("/search_store_date")
    public ResponseEntity<List<GetPopupStoreRes>> search_store_date(@RequestParam String storeDate) {
        List<GetPopupStoreRes> popupStoreResList = popupStoreService.findByStoreDate(storeDate);
        return ResponseEntity.ok(popupStoreResList);
    }

    @GetMapping("/search-company-idx")
    public ResponseEntity<List<GetPopupStoreRes>> search_company_idx(@RequestParam Long companyIdx) {
        List<GetPopupStoreRes> popupStoreResList = popupStoreService.findByCompanyIdx(companyIdx);
        return ResponseEntity.ok(popupStoreResList);
    }
}
