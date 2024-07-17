package com.fiiiiive.zippop.test;

import com.fiiiiive.zippop.common.baseresponse.BaseResponse;
import com.fiiiiive.zippop.common.baseresponse.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Tag(name = "test-api", description = "Test Application")
@Slf4j
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @Operation(summary = "ex01")
    @RequestMapping(method = RequestMethod.GET, value = "/ex01")
    public ResponseEntity<BaseResponse> ex01(){
        return ResponseEntity.ok(new BaseResponse("ex01", BaseResponseMessage.REQUEST_SUCCESS));
    }

}
