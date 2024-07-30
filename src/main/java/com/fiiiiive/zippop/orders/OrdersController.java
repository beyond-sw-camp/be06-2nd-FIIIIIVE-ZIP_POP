package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.orders.model.response.GetOrdersRes;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "orders-api", description = "Orders")
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;
    @GetMapping("/verify")
    public BaseResponse<GetOrdersRes> verify(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam String impUid,
        @RequestParam int operation) throws BaseException, IamportResponseException, IOException{
        GetOrdersRes response = ordersService.verify(customUserDetails, impUid, operation);
        return new BaseResponse(BaseResponseMessage.POPUP_PAY_SUCCESS,response);
    }
}