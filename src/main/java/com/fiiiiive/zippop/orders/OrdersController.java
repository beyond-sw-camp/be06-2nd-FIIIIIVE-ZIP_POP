package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.orders.model.response.GetOrdersRes;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrdersController {
    private final OrdersService ordersService;
    @RequestMapping(method = RequestMethod.GET, value = "/validation")
    public BaseResponse<GetOrdersRes> validation(@AuthenticationPrincipal CustomUserDetails customUserDetails, String impUid) throws Exception, IamportResponseException, IOException {
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
        GetOrdersRes response = ordersService.paymentValidation(customer, impUid);
        return new BaseResponse(BaseResponseMessage.POPUP_GOODS_ORDERS_VALIDATION_SUCCESS, response);
    }
}
