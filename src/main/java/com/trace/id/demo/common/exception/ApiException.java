package com.trace.id.demo.common.exception;

import com.trace.id.demo.enums.response.ErrorCode;

public class ApiException extends BaseCustomException {

    public ApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
