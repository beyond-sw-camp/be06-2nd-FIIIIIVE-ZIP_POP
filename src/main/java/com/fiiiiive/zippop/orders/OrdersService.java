package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.orders.model.CompanyOrders;
import com.fiiiiive.zippop.orders.model.CustomerOrders;
import com.fiiiiive.zippop.orders.model.CustomerOrdersDetail;
import com.fiiiiive.zippop.orders.model.CompanyOrdersDetail;
import com.fiiiiive.zippop.orders.model.response.GetOrdersRes;
import com.fiiiiive.zippop.popup_goods.PopupGoodsRepository;
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
import java.util.Optional;

import static com.fiiiiive.zippop.common.responses.BaseResponseMessage.*;


@Service
@RequiredArgsConstructor
public class OrdersService {
    private final IamportClient iamportClient;
    private final CustomerOrdersDetailRepository ordersDetailRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CustomerOrdersDetailRepository customerOrdersDetailRepository;
    private final CompanyOrdersDetailRepository companyOrdersDetailRepository;
    private final CustomerOrdersRepository customerOrdersRepository;
    private final CompanyOrdersRepository companyOrdersRepository;
    private final PopupGoodsRepository popupGoodsRepository;
    private final PopupStoreRepository popupStoreRepository;

    @Transactional
    public GetOrdersRes validation(CustomUserDetails customUserDetails, String impUid, Integer operation) throws BaseException, IamportResponseException, IOException {
        // 기업회원 등록 수수료 결제(operation = 0 )
        if (customUserDetails.getRole().equals("ROLE_COMPANY") && operation == 0) {
            Optional<Company> result = companyRepository.findById(customUserDetails.getIdx());
            if (result.isPresent()) {
                Company company = result.get();
                IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
                Payment payment = response.getResponse();
                if (payment == null) {
                    throw new BaseException(POPUP_STORE_PAY_FAIL_NOT_FOUND_PAYINFO);
                }
                Integer payedPrice = payment.getAmount().intValue();
                String customData = payment.getCustomData();
                Gson gson = new Gson();
                Map<String, Object > customDataMap = gson.fromJson(customData, Map.class);
                if (customDataMap.get("storeIdx") == null) {
                    throw new BaseException(POPUP_STORE_PAY_FAIL_INCORRECT_REQUEST);
                }
                Long storeIdx = ((Double)customDataMap.get("storeIdx")).longValue();
                PopupStore popupStore = popupStoreRepository.findById(storeIdx)
                        .orElseThrow(() -> new BaseException(POPUP_STORE_PAY_FAIL_NOT_FOUND_STORE));
                Integer totalPeople = popupStore.getTotalPeople();
                Integer expectedPrice = totalPeople * 1500;
                if (!payedPrice.equals(expectedPrice)) {
                    throw new BaseException(BaseResponseMessage.POPUP_STORE_PAY_FAIL_VALIDATION_ERROR);
                }
                CompanyOrders companyOrders = CompanyOrders.builder()
                        .impUid(impUid)
                        .company(company)
                        .build();
                companyOrdersRepository.save(companyOrders);
                CompanyOrdersDetail companyOrdersDetail = CompanyOrdersDetail.builder()
                        .companyOrders(companyOrders)
                        .totalPrice(expectedPrice)
                        .popupStore(popupStore)
                        .build();
                companyOrdersDetailRepository.save(companyOrdersDetail);
                return GetOrdersRes.builder()
                        .impUid(impUid)
                        .storeIdx(storeIdx)
                        .totalPrice(expectedPrice)
                        .totalPeople(totalPeople)
                        .build();
            } else {
                throw new BaseException(POPUP_STORE_PAY_FAIL_NOT_FOUND_COMPANY);
            }
        }
        else if (customUserDetails.getRole().equals("ROLE_CUSTOMER")) {
            Optional<Customer> result = customerRepository.findById(customUserDetails.getIdx());
            if (result.isPresent()) {
                Customer customer = result.get();
                IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
                Payment payment = response.getResponse();
                if (payment == null) {
                    throw new BaseException(POPUP_STORE_PAY_FAIL);
                }
                Integer payedPrice = payment.getAmount().intValue();
                String customData = payment.getCustomData();
                if (customData == null) {
                    throw new BaseException(POPUP_STORE_PAY_FAIL);
                }
                Gson gson = new Gson();
                Map<String, Double> goodsMap = gson.fromJson(customData, Map.class);
                Integer totalPurchasePrice = 0;
                Integer addPoint = 0;
                Integer usedPoint = 0;
                List<PopupGoods> popupGoodsList = new ArrayList<>();
                if (operation == 1) { // 사전 굿즈 구매(operation = 1)
                    for (String key : goodsMap.keySet()) {
                        Integer purchaseGoodsPrice = goodsMap.get(key).intValue();
                        PopupGoods popupGoods = popupGoodsRepository.findByIdx(Long.parseLong(key)).orElseThrow(() -> new BaseException(POPUP_GOODS_PAY_GOODS_NULL));
                        if (purchaseGoodsPrice != 1) {
                            throw new BaseException(POPUP_GOODS_PAY_FAIL_LIMIT_EXCEEDED);
                        }
                        popupGoodsList.add(popupGoods);
                        totalPurchasePrice += purchaseGoodsPrice * popupGoods.getProductPrice();
                    }
                    addPoint += (int)Math.round(totalPurchasePrice * 0.05);
                    usedPoint = (totalPurchasePrice + 500) - payedPrice;
                    if(customer.getPoint() < 3000 || customer.getPoint() - usedPoint < 0) { throw new BaseException(POPUP_GOODS_PAY_FAIL_POINT_EXCEEDED); }
                    totalPurchasePrice += 500 - usedPoint;
                    customer.setPoint(customer.getPoint() - usedPoint + addPoint);
                }
                else { // 재고 굿즈 구매 (operation = 2)

                    for (String key : goodsMap.keySet()) {
                        Integer purchaseGoodsPrice = goodsMap.get(key).intValue();
                        PopupGoods popupGoods = popupGoodsRepository.findByIdx(Long.parseLong(key)).orElseThrow(() -> new BaseException(POPUP_GOODS_PAY_GOODS_NULL));
                        if (purchaseGoodsPrice > popupGoods.getProductAmount()) {
                            throw new BaseException(POPUP_GOODS_PAY_FAIL_EXCEEDED);
                        }
                        popupGoodsList.add(popupGoods);
                        totalPurchasePrice += purchaseGoodsPrice * popupGoods.getProductPrice();
                    }
                    usedPoint = (totalPurchasePrice + 2500) - payedPrice;
                    if(customer.getPoint() < 3000 || customer.getPoint() - usedPoint < 0) { throw new BaseException(POPUP_GOODS_PAY_FAIL_POINT_EXCEEDED); }
                    totalPurchasePrice += 2500 - usedPoint;
                    customer.setPoint(customer.getPoint() - usedPoint);
                }
                if (payedPrice.equals(totalPurchasePrice)) {
                    customerRepository.save(customer);
                    CustomerOrders customerOrders = CustomerOrders.builder()
                            .impUid(impUid)
                            .totalPrice(totalPurchasePrice)
                            .usedPoint(usedPoint)
                            .customer(customer)
                            .build();
                    customerOrdersRepository.save(customerOrders);
                    for (String key : goodsMap.keySet()) {
                        Integer purchaseGoodsAmount = goodsMap.get(key).intValue();
                        PopupGoods popupGoods = popupGoodsRepository.findById(Long.parseLong(key)).orElseThrow(() -> new BaseException(POPUP_GOODS_PAY_GOODS_NULL));
                        popupGoods.setProductAmount(popupGoods.getProductAmount() - purchaseGoodsAmount);
                        popupGoodsRepository.save(popupGoods);
                    }
                    for (PopupGoods popupGoods : popupGoodsList) {
                        if (operation == 1) {
                            CustomerOrdersDetail customerOrdersDetail = CustomerOrdersDetail.builder()
                                    .orderState("in reserve")
                                    .customerOrders(customerOrders)
                                    .deliveryCost(0)
                                    .popupGoods(popupGoods)
                                    .build();
                            customerOrdersDetailRepository.save(customerOrdersDetail);
                        } else {
                            CustomerOrdersDetail customerOrdersDetail = CustomerOrdersDetail.builder()
                                    .orderState("in delivery")
                                    .customerOrders(customerOrders)
                                    .deliveryCost(2500)
                                    .popupGoods(popupGoods)
                                    .build();
                            customerOrdersDetailRepository.save(customerOrdersDetail);
                        }
                    }
                    return GetOrdersRes.builder()
                            .impUid(impUid)
                            .productIdxMap(goodsMap)
                            .totalPrice(totalPurchasePrice)
                            .build();
                }
                else {
                    throw new BaseException(POPUP_GOODS_PAY_FAIL_VALIDATION_ERROR);
                }
            }
            else {
                throw new BaseException(POPUP_GOODS_PAY_FAIL_NOT_FOUND_MEMBER);
            }
        }
        else {
            throw new BaseException(POPUP_PAY_FAIL_NOT_INVALID);
        }
    }
}

