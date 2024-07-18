package com.fiiiiive.zippop.test;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "test-api", description = "Test")
@Slf4j
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @Operation(summary = "exception")
    @RequestMapping(method = RequestMethod.GET, value = "/exception")
    public ResponseEntity<BaseResponse> exception(){
//        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.REQUEST_SUCCESS, "ex01"));
        throw new RuntimeException("Test exception");
    }

    @Operation(summary = "baseResponse")
    @RequestMapping(method = RequestMethod.GET, value = "/base-response")
    public ResponseEntity<BaseResponse> baseResponse(){
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.REQUEST_SUCCESS, "ex01"));
    }

    @Operation(summary = "baseException")
    @RequestMapping(method = RequestMethod.GET, value = "/base-exception")
    public ResponseEntity<BaseResponse> baseException() throws BaseException {
        throw new BaseException(BaseResponseMessage.REQUEST_FAIL);
    }
}
