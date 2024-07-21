package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.orders.model.Orders;
import com.fiiiiive.zippop.orders.model.OrdersDetail;
import com.fiiiiive.zippop.orders.model.response.GetOrdersRes;
import com.fiiiiive.zippop.popup_goods.PopupGoodsRepository;
import com.fiiiiive.zippop.popup_goods.PopupGoodsService;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_store.PopupStoreRepository;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fiiiiive.zippop.common.responses.BaseResponseMessage.*;


@Service
@RequiredArgsConstructor
public class OrdersService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;

    // 재고 굿즈 구매
    @Transactional
    public GetOrdersRes stockGoodsPaymentValidation(CustomUserDetails customUserDetails, String impUid) throws BaseException, IamportResponseException, IOException {
        return validateAndProcessPayment(customUserDetails, impUid, false);
    }

    // 사전 굿즈 구매
    @Transactional
    public GetOrdersRes reservationGoodsPaymentValidation(CustomUserDetails customUserDetails, String impUid) throws BaseException, IamportResponseException, IOException {
        return validateAndProcessPayment(customUserDetails, impUid, true);
    }

    // 팝업스토어 등록 결제 검증
    @Transactional
    public GetOrdersRes popupStoreRegistrationValidation(CustomUserDetails customUserDetails, String impUid) throws BaseException, IamportResponseException, IOException {
        // 기업회원 확인
        if (customUserDetails == null || !customUserDetails.getRole().equals("ROLE_COMPANY")) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REGISTER_FAIL_UNAUTHORIZED);
        }

        // iamport에서 결제 정보를 받아옴
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();
        if (payment == null) {
            throw new BaseException(POPUP_STORE_REGISTER_FAIL_VALIDATION_FAIL);
        }

        // 결제된 가격을 저장
        Integer payedAmount = payment.getAmount().intValue();

        // custom_data에서 storeIdx 추출
        String customData = payment.getCustomData();
        if (customData == null) {
            throw new BaseException(POPUP_STORE_REGISTER_FAIL_VALIDATION_FAIL);
        }

        Gson gson = new Gson();
        Map<String, Object> customDataMap = gson.fromJson(customData, Map.class);

        Long storeIdx = ((Double) customDataMap.get("storeIdx")).longValue();

        // 팝업스토어 정보 조회
        PopupStore popupStore = popupStoreRepository.findById(storeIdx)
                .orElseThrow(() -> new BaseException(POPUP_STORE_SEARCH_FAIL_NOT_EXIST));

        // 데이터베이스에서 totalPeople 값 가져오기
        Integer totalPeople = popupStore.getTotalPeople();

        // expected amount 계산
        Integer expectedAmount = totalPeople * 1500;

        // 결제 금액이 맞는지 확인
        if (!payedAmount.equals(expectedAmount)) {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REGISTER_FAIL_VALIDATION_FAIL);
        }

        // 결제 내역 저장
        return GetOrdersRes.builder()
                .impUid(impUid)
                .storeIdx(storeIdx)
                .totalPeople(totalPeople)
                .build();
    }


    private GetOrdersRes validateAndProcessPayment(CustomUserDetails customUserDetails, String impUid, boolean isReservation) throws BaseException, IamportResponseException, IOException {
        Customer customer = null;
        if (customUserDetails != null) {
            customer = Customer.builder()
                    .customerIdx(customUserDetails.getIdx())
                    .email(customUserDetails.getEmail())
                    .password(customUserDetails.getPassword())
                    .role(customUserDetails.getRole())
                    .enabled(customUserDetails.getEnabled())
                    .build();
        }

        // iamport에서 결제 정보를 받아옴
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();
        if (payment == null) {
            throw new BaseException(POPUP_GOODS_ORDERS_VALIDATION_FAIL);
        }

        // 결제된 가격을 저장
        Integer payedAmount = payment.getAmount().intValue();
        // 결제된 상품의 데이터 [굿즈 상품 번호 : 갯수]를 저장
        String customData = payment.getCustomData();
        if (customData == null) {
            throw new BaseException(POPUP_GOODS_ORDERS_VALIDATION_FAIL);
        }

        Gson gson = new Gson();
        Map<String, Double> goodsMap = gson.fromJson(customData, Map.class);

        Integer totalPurchaseGoodsAmount = 0;
        List<PopupGoods> popupGoodsList = new ArrayList<>();
        // key 변수가 goods 번호로 DB에서 goods의 가격을 조회
        for (String key : goodsMap.keySet()) {
            // 굿즈 번호에 해당하는 굿즈를 구매한 갯수 저장
            Integer purchaseGoodsAmount = goodsMap.get(key).intValue();

            // 사전 굿즈 구매인 경우, 수량이 1이 아니면 에러 리턴
            if (isReservation && purchaseGoodsAmount != 1) {
                throw new BaseException(POPUP_GOODS_ORDER_LIMIT_EXCEEDED);
            }

            // DB에서 조회한 가격 * 구매한 갯수를 반복문 돌면서 다 더한다.
            PopupGoods popupGoods = popupGoodsRepository.findByIdx(Long.parseLong(key)).orElseThrow(() -> new BaseException(POPUP_GOODS_ORDERS_GOODS_NULL));
            // 굿즈 재고보다 더 많은 갯수를 구매하려고 하면 에러 리턴
            if (purchaseGoodsAmount > popupGoods.getProductAmount()) {
                throw new BaseException(POPUP_GOODS_ORDERS_FAIL_EXCEEDED);
            }
            popupGoodsList.add(popupGoods);
            totalPurchaseGoodsAmount += purchaseGoodsAmount * popupGoods.getProductPrice(); // * 조회한 가격
        }

        // 결제 금액이 맞으면
        if (payedAmount.equals(totalPurchaseGoodsAmount)) {
            // 결제 내역 저장
            Orders orders = Orders.builder()
                    .impUid(impUid)
                    .customer(customer)
                    .build();
            ordersRepository.save(orders);

            // 팝업 굿즈 재고 수정
            for (String key : goodsMap.keySet()) {
                // 굿즈 번호에 해당하는 굿즈를 구매한 갯수 저장
                Integer purchaseGoodsAmount = goodsMap.get(key).intValue();
                PopupGoods popupGoods = popupGoodsRepository.findById(Long.parseLong(key)).orElseThrow(() -> new BaseException(POPUP_GOODS_ORDERS_GOODS_NULL));
                popupGoods.setProductAmount(popupGoods.getProductAmount() - purchaseGoodsAmount);

                popupGoodsRepository.save(popupGoods);
            }

            for (PopupGoods popupGoods : popupGoodsList) {
                OrdersDetail ordersDetail = OrdersDetail.builder()
                        .orders(orders)
                        .popupGoods(popupGoods)
                        .build();

                ordersDetailRepository.save(ordersDetail);
            }

            return GetOrdersRes.builder()
                    .impUid(impUid)
                    .productIdxMap(goodsMap)
                    .build();
        } else {
            throw new BaseException(POPUP_GOODS_ORDERS_VALIDATION_FAIL);
        }
    }
}
