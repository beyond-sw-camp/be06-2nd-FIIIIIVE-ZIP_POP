package com.fiiiiive.zippop.popup_goods;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.common.annotation.ExeTimer;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.request.CreatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.request.UpdatePopupGoodsReq;
import com.fiiiiive.zippop.popup_goods.model.response.CreatePopupGoodsRes;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_goods.model.response.UpdatePopupGoodsRes;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.request.UpdatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.UpdatePopupStoreRes;
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
import java.util.Optional;

@Tag(name = "popup-goods-api", description = "PopupGoods")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-goods")
public class PopupGoodsController {
    private final PopupGoodsService popupGoodsService;
    private final CloudFileUpload cloudFileUpload;

    // 팝업 굿즈 수정 등록
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long storeIdx,
        @RequestPart("files") MultipartFile[] files,
        @RequestPart("dto") CreatePopupGoodsReq dto) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        CreatePopupGoodsRes response = popupGoodsService.register(customUserDetails, storeIdx, fileNames, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_REGISTER_SUCCESS, response));
    }

    // 상품 이름으로 조회
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> search(
            @RequestParam String productName,
            @RequestParam int page,
            @RequestParam int size) throws Exception {
        Page<GetPopupGoodsRes> popupGoodsPage = popupGoodsService.search(productName, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoodsPage));
    }

    // 팝업스토어 인덱스로 조회
    @GetMapping("/search-store")
    public ResponseEntity<BaseResponse<Page<GetPopupGoodsRes>>> searchStore(
        @RequestParam Long storeIdx,
        @RequestParam int page,
        @RequestParam int size) throws BaseException {
        Page<GetPopupGoodsRes> popupGoodsPage = popupGoodsService.searchStore(storeIdx, page, size);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_GOODS_SEARCH_SUCCESS, popupGoodsPage));
    }

    // 팝업 굿즈 수정
    @PatchMapping("/update")
    public ResponseEntity<BaseResponse> update(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long productIdx,
        @RequestPart(name = "dto") UpdatePopupGoodsReq dto,
        @RequestPart(name = "files") MultipartFile[] files) throws BaseException {
        List<String> fileNames = cloudFileUpload.multipleUpload(files);
        UpdatePopupGoodsRes response = popupGoodsService.update(customUserDetails, productIdx, fileNames, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_UPDATE_SUCCESS,response));
    }

    // 팝업 굿즈 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam Long productIdx) throws BaseException {
        popupGoodsService.delete(customUserDetails, productIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.POPUP_STORE_DELETE_SUCCESS));
    }
}
