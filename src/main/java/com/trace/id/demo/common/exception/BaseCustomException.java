package com.trace.id.demo.common.exception;

import com.trace.id.demo.enums.response.ErrorCode;
import lombok.Getter;

@Getter
public class BaseCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseCustomException(ErrorCode errorCode) {
        super(errorCode.getErrorEngMessage());
        this.errorCode = errorCode;
    }
}
