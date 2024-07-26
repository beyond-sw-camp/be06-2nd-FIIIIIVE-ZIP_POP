package com.fiiiiive.zippop.popup_reserve;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_reserve.model.PopupReserve;
import com.fiiiiive.zippop.popup_reserve.model.request.CreatePopupReserveReq;
import com.fiiiiive.zippop.popup_reserve.model.response.CreatePopupReserveRes;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.utils.JwtUtil;
import com.fiiiiive.zippop.utils.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopupReserveService {
    private final PopupReserveRepository popupReserveRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public CreatePopupReserveRes create(CustomUserDetails customUserDetails, CreatePopupReserveReq dto) throws BaseException {
        if(Objects.equals(customUserDetails.getRole(), "ROLE_COMPANY")){
            Optional<PopupStore> result = popupStoreRepository.findById(dto.getStoreIdx());
            if(result.isPresent()){
                PopupStore popupStore = result.get();
                String reserveUUID = UUID.randomUUID().toString();
                String reserveWaitingUUID = UUID.randomUUID().toString();
                PopupReserve popupReserve = PopupReserve.builder()
                        .maxCount(dto.getMaxCount())
                        .popupStore(popupStore)
                        .reserveUUID(reserveUUID)
                        .reserveWaitingUUID(reserveWaitingUUID)
                        .expiredTime(dto.getExpireTime())
                        .build();
                popupReserveRepository.save(popupReserve);
                redisUtil.init(reserveUUID, popupReserve.getExpiredTime()); // 예약 접속
                redisUtil.init(reserveWaitingUUID, popupReserve.getExpiredTime()); // 예약 대기
                return CreatePopupReserveRes.builder()
                        .reserveIdx(popupReserve.getReserveIdx())
                        .reserveUUID(popupReserve.getReserveUUID())
                        .reserveWaitingUUID(popupReserve.getReserveWaitingUUID())
                        .storeIdx(popupStore.getStoreIdx())
                        .build();
            } else {
                throw new BaseException(BaseResponseMessage.POPUP_RESERVE_CREATE_FAIL_NOT_FOUND);
            }
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_RESERVE_CREATE_FAIL_INVALID_MEMBER);
        }
    }

    public String enroll(HttpServletRequest req, HttpServletResponse res, String email, Long reserveIdx) throws IOException {
        // String email = customUserDetails.getEmail();
        Optional<PopupReserve> result = popupReserveRepository.findById(reserveIdx);
        PopupReserve popupReserve = result.get();
        String reserveUUID = popupReserve.getReserveUUID();
        String reserveWaitingUUID = popupReserve.getReserveWaitingUUID();
        // 만약 최대 예약자보다 redis의 값의 크기가 적으면 예약 redis로 이동하거나 취소처리로 자리가 났을 경우에
        if(popupReserve.getMaxCount() > redisUtil.zCard(reserveUUID) || redisUtil.getzRank(reserveUUID, email) != null){
            // 예약 redis의 자리가 있을때
            if(redisUtil.getzRank(reserveUUID, email) == null){
                redisUtil.save(reserveUUID, email, System.currentTimeMillis());
            }
             // accessToken 만들어줌
            String token = jwtUtil.createToken(reserveIdx, email);
            res.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            Cookie aToken = new Cookie("ReserveToken", token);
            aToken.setHttpOnly(true);
            aToken.setSecure(true);
            aToken.setPath("/");
            aToken.setMaxAge(60 * 60 * 1);
            res.addCookie(aToken);
            // 예약 굿즈 구매 사이트로 이동 리다이렉션? 토큰이 없는 유저가 예약굿즈 구매 접근시 뒤로 보내야됨
            Long rank = redisUtil.getzRank(reserveUUID, email)+1L;
            return "팝업 스토어 예약 굿즈 페이지로 이동합니다 -> 접속 번호" + rank;
        }  else { // 예약 redis가 꽉차면 대기 redis로 이동
            redisUtil.save(reserveWaitingUUID, email, System.currentTimeMillis());
            Long rank = redisUtil.getzRank(reserveWaitingUUID, email) + 1L;
            return "예약 대기: "+rank;
        }
    }

    public String cancel(String email, Long reserveIdx) throws BaseException {
        // String email = customUserDetails.getEmail();

        Optional<PopupReserve> result = popupReserveRepository.findById(reserveIdx);
        if (result.isPresent()) {
            PopupReserve popupReserve = result.get();
            String reserveUUID = popupReserve.getReserveUUID();
            String reserveWaitingUUID = popupReserve.getReserveWaitingUUID();
            // 현재 사용자 예약 접속 redis에서 삭제
            redisUtil.remove(reserveUUID, email);
            // 대기1순위 사용자 예약 접속 redis에 추가
            String movedUser = redisUtil.moveFirstWaitingToReserve(reserveUUID, reserveWaitingUUID);
            if (movedUser != null) { return "대기자에서 예약자로 이동: " + movedUser; }
            return "대기자가 없습니다.";
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_RESERVE_CANCEL_FAIL);
        }
    }

    public String reserveStatus(String reserveUUID, String reserveWaitingUUID){
        String reserveTotal = redisUtil.getAllValues(reserveUUID);
        String reserveWaitingTotal = redisUtil.getAllValues(reserveWaitingUUID);
        return " 예약접속자 " + reserveTotal + " 예약대기자 " + reserveWaitingTotal;
    }
}

