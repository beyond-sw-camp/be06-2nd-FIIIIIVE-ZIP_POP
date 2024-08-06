package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.request.UpdatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.CreatePopupStoreRes;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import com.fiiiiive.zippop.popup_store.model.response.UpdatePopupStoreRes;
import com.fiiiiive.zippop.post.model.request.UpdatePostReq;
import com.fiiiiive.zippop.post.model.response.UpdatePostRes;
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

import java.time.LocalDateTime;
import java.util.List;


@Tag(name = "popup-store-api", description = "PopupStore")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-store")
public class PopupStoreController {
    private final PopupStoreService popupStoreService;
    private final CloudFileUpload cloudFileUpload;

    // 팝업 스토어 등록
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestPart("files") MultipartFile[] files,
        @RequestPart("dto") CreatePopupStoreReq dto) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        CreatePopupStoreRes response = popupStoreService.register(customUserDetails, dto, fileNames);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_REGISTER_SUCCESS, response));
    }

    // 팝업 스토어 단일 조회
    // @ExeTimer
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<GetPopupStoreRes>> search(
        @RequestParam Long storeIdx) throws BaseException {
        GetPopupStoreRes getPopupStoreRes = popupStoreService.search(storeIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, getPopupStoreRes));
    }

    // 팝업 스토어 전체 조회
    // @ExeTimer
    @GetMapping("/search-all")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchAll (
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreList = popupStoreService.searchAll(page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreList));
    }


    // 기업이 등록한 팝업 스토어 조회
    // @ExeTimer
    @GetMapping("/search-company")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchCompany(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.searchCompany(customUserDetails, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    // 팝업스토어 키워드 기반 검색
    // @ExeTimer
    @GetMapping("/search-keyword")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchKeyword(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.searchKeyword(keyword, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    // 팝업스토어 날짜 범위 기반 검색
    @GetMapping("/search-daterange")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchStoreDate(
            @RequestParam LocalDateTime storeStartDate,
            @RequestParam LocalDateTime storeEndDate,
            @RequestParam int page,
            @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.searchDateRange(storeStartDate, storeEndDate, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    // 팝업 스토어 수정
    @PatchMapping("/update")
    public ResponseEntity<BaseResponse> update(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long storeIdx,
        @RequestPart(name = "dto") UpdatePopupStoreReq dto,
        @RequestPart(name = "files") MultipartFile[] files) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        UpdatePopupStoreRes response = popupStoreService.update(customUserDetails, storeIdx, dto, fileNames);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_UPDATE_SUCCESS,response));
    }

    // 팝업 스토어 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long storeIdx) throws BaseException {
        popupStoreService.delete(customUserDetails, storeIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_DELETE_SUCCESS));
    }

    // 팝업 스토어 좋아요
    @GetMapping("/like")
    public ResponseEntity<BaseResponse> like(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long storeIdx) throws BaseException {
        popupStoreService.like(customUserDetails, storeIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_LIKE_SUCCESS));
    }

    @GetMapping("/search-category")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchCategory(
            @RequestParam String category,
            @RequestParam int page,
            @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreResList = popupStoreService.searchCategory(category, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResList));
    }

    @GetMapping("/search-store")
    public ResponseEntity<BaseResponse<GetPopupStoreRes>> searchStore(
        @RequestParam String storeName) throws BaseException {
        GetPopupStoreRes getPopupStoreRes = popupStoreService.searchStore(storeName);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, getPopupStoreRes));
    }

    @GetMapping("/search-address")
    public ResponseEntity<BaseResponse<Page<GetPopupStoreRes>>> searchAddress(
        @RequestParam String storeAddress,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupStoreRes> popupStoreResPage = popupStoreService.searchAddress(storeAddress, page, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.POPUP_STORE_SEARCH_SUCCESS, popupStoreResPage));
    }
}
