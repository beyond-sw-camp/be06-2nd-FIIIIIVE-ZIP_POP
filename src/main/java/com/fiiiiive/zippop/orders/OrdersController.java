package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.baseresponse.BaseException;
import com.fiiiiive.zippop.common.baseresponse.BaseResponse;
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

import static com.fiiiiive.zippop.common.baseresponse.BaseResponseStatus.IAMPORT_ERROR;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrdersController {
    private final OrdersService ordersService;
    @RequestMapping(method = RequestMethod.GET, value = "/validation")
    public BaseResponse<GetOrdersRes> validation(@AuthenticationPrincipal CustomUserDetails customUserDetails, String impUid) {
        System.out.println(customUserDetails.getRole());
        try {
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
            return new BaseResponse<>(response);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (IamportResponseException iamportResponseException) {
            return new BaseResponse<>(IAMPORT_ERROR);
        } catch (IOException exception) {
            return new BaseResponse<>(IAMPORT_ERROR);
        }
    }
}
