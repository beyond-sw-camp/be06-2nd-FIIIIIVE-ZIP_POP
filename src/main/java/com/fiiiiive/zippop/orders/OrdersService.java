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

import static com.fiiiiive.zippop.common.responses.BaseResponseMessage.GOODS_ORDER_FAIL_EXCEEDED;
import static com.fiiiiive.zippop.common.responses.BaseResponseMessage.ORDERS_VALIDATION_FAIL;


@Service
@RequiredArgsConstructor
public class OrdersService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupGoodsService popupGoodsService;

    @Transactional
    public GetOrdersRes paymentValidation(CustomUserDetails customUserDetails, String impUid) throws BaseException, IamportResponseException, IOException {
        Customer customer = null;
        if (customUserDetails != null) {
            customer = Customer.builder()
                    .idx(customUserDetails.getIdx())
                    .email(customUserDetails.getEmail())
                    .password(customUserDetails.getPassword())
                    .role(customUserDetails.getRole())
                    .enabled(customUserDetails.getEnabled())
                    .build();
        }

        // iamport에서 결제 정보를 받아옴
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        // 결제된 가격을 저장
        Integer payedAmount = response.getResponse().getAmount().intValue();
        // 결제된 상품의 데이터 [굿즈 상품 번호 : 갯수]를 저장
        String customData = response.getResponse().getCustomData();
        Gson gson = new Gson();
        Map<String, Double> goodsMap = gson.fromJson(customData, Map.class);

        Integer totalPurchaseGoodsAmount = 0;
        List<PopupGoods> popupGoodsList = new ArrayList<>();
        // key 변수가 goods 번호로 DB에서 goods의 가격을 조회
        for (String key : goodsMap.keySet()) {
            // 굿즈 번호에 해당하는 굿즈를 구매한 갯수 저장
            Integer purchaseGoodsAmount = goodsMap.get(key).intValue();
            // DB에서 조회한 가격 * 구매한 갯수를 반복문 돌면서 다 더한다.
            PopupGoods popupGoods = popupGoodsRepository.findByIdx(Long.parseLong(key)).orElseThrow();
            // 굿즈 재고보다 더 많은 갯수를 구매하려고 하면 에러 리턴
            if (purchaseGoodsAmount > popupGoods.getProductAmount()) {
                throw new BaseException(GOODS_ORDER_FAIL_EXCEEDED);
            }
            popupGoodsList.add(popupGoods);
            totalPurchaseGoodsAmount +=  purchaseGoodsAmount * popupGoods.getProductPrice(); // * 조회한 가격

        }

        // 결제 금액이 맞으면
        if(payedAmount.equals(totalPurchaseGoodsAmount)) {
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
                PopupGoods popupGoods = popupGoodsRepository.findById(Long.parseLong(key)).orElseThrow();
                popupGoods.setProductAmount(popupGoods.getProductAmount() - purchaseGoodsAmount);

                popupGoodsRepository.save(popupGoods);
            }

            List<PopupGoods> popupGoodsList11 = new ArrayList<>();

            for(PopupGoods popupGoods : popupGoodsList) {
                OrdersDetail ordersDetail = OrdersDetail.builder()
                        .orders(orders)
                        .popupGoods(popupGoods)
                        .build();

                popupGoodsRepository.findById(popupGoods.getProductIdx());
            }

            return GetOrdersRes.builder()
                    .impUid(impUid)
                    .productIdxMap(goodsMap)
                    .build();
        } else {
            throw new BaseException(ORDERS_VALIDATION_FAIL);
        }
    }
}
