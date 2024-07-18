package com.fiiiiive.zippop.common.exception;

import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleException(Exception e) {
        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<String>> handleBaseException(BaseException e){
        return ResponseEntity.badRequest().body(new BaseResponse(BaseResponseMessage.findByCode(e.getCode())));
    }
//    private ResponseEntity<ErrorResponse> makeErrorResponse(
//            ErrorCode errorCode
//    ){
//        return new ResponseEntity<>(
//                new ErrorResponse(errorCode.getCode(), errorCode.getMessage()),
//                errorCode.getHttpStatus()
//        );
//    }
}