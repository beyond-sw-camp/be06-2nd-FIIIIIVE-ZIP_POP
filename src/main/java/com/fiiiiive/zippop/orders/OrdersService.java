package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OrdersService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupGoodsService popupGoodsService;

    public GetOrdersRes paymentValidation(Customer customer, String impUid) throws BaseException, IamportResponseException, IOException {
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
            PopupGoods popupGoods = popupGoodsRepository.findById(Long.parseLong(key)).orElseThrow();
            popupGoodsList.add(popupGoods);
            totalPurchaseGoodsAmount +=  purchaseGoodsAmount * popupGoods.getProductPrice(); // * 조회한 가격
            System.out.println(purchaseGoodsAmount);
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

            for(PopupGoods popupGoods : popupGoodsList) {
                OrdersDetail ordersDetail = OrdersDetail.builder()
                        .orders(orders)
                        .popupGoods(popupGoods)
                        .build();

                popupGoodsRepository.findById(popupGoods.getProductIdx());
            }

            return GetOrdersRes.builder().impUid(impUid).build();
        } else {
            throw new BaseException(BaseResponseMessage.ORDERS_VALIDATION_FAIL);
        }
    }

//    public Boolean checkOrdered(Customer customer, Long productIdx) {
//        Optional<Orders> result = ordersRepository.findByCustomerAndPopupGoods(customer, PopupGoods.builder().productIdx(productIdx).build());
//
//        if (result.isPresent()) {
//            return true;
//        }
//        return false;
//    }
}
