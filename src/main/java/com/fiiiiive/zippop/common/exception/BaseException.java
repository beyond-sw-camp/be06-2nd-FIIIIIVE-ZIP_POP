package com.fiiiiive.zippop.common.exception;

import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private String message;
    private Integer code;
    private String details;

    public BaseException(BaseResponseMessage baseResponseMessage) {
        this.code = baseResponseMessage.getCode();
        this.message = baseResponseMessage.getMessage();
    }

    public BaseException(BaseResponseMessage baseResponseMessage, String details) {
        this.code = baseResponseMessage.getCode();
        this.message = baseResponseMessage.getMessage();
        this.details = details;
    }
}


