package com.fiiiiive.zippop.common.exception;

import com.fiiiiive.zippop.common.responses.BaseResponse;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<BaseResponse<String>> handleException(Exception e) {
//        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
//        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<BaseResponse> handleMailException(MailException e){
        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.MEMBER_EMAIL_SEND_FAIL, e.getMessage());
        return ResponseEntity.badRequest().body(baseResponse);
    }

    @ExceptionHandler(IamportResponseException.class)
    public ResponseEntity<BaseResponse> handleIamportResponseException(IamportResponseException e){
        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.badRequest().body(baseResponse);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<String>> handleBaseException(BaseException e){
        return ResponseEntity.badRequest().body(new BaseResponse(BaseResponseMessage.findByCode(e.getCode())));
    }

}