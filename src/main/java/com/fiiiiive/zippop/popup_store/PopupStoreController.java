package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import com.fiiiiive.zippop.utils.CloudFileUpload;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "popup-store-api", description = "PopupStore")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-store")
public class PopupStoreController {
    private final PopupStoreService popupStoreService;
    private final CloudFileUpload cloudFileUpload;

    @ExeTimer
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestPart("files") MultipartFile[] files,
        @RequestPart("dto") CreatePopupStoreReq createPopupStoreReq) throws Exception {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        popupStoreService.register(customUserDetails, createPopupStoreReq, fileNames);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_REGISTER_SUCCESS));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> search(
        @RequestParam int page,
        @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupStoreRes> popupStoreList = popupStoreService.findAll(pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreList));
    }

    @GetMapping("/search-category")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> search_category(@RequestParam String category, @RequestParam int page,
                                                                                @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.findByCategory(category, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    @GetMapping("/search-store-name")
    public ResponseEntity<BaseResponse<GetPopupStoreRes>> search_store_name(@RequestParam String storeName) throws Exception {
        GetPopupStoreRes getPopupStoreRes = popupStoreService.findByStoreName(storeName);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, getPopupStoreRes));
    }

    @GetMapping("/search-store-addr")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> search_store_addr(@RequestParam String storeAddr, @RequestParam int page,
                                                                                  @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupStoreRes> popupStoreResPage = popupStoreService.findByStoreAddr(storeAddr, pageable);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResPage));
    }


    @GetMapping("/search_store_date")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> search_store_date(@RequestParam String storeDate, @RequestParam int page,
                                                                                  @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.findByStoreDate(storeDate, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    @GetMapping("/search-company-idx")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> search_company_idx(@RequestParam Long companyIdx, @RequestParam int page,
                                                                                   @RequestParam int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.findByCompanyIdx(companyIdx, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }
}
