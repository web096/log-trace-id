package com.trace.id.demo.common.exception;

import com.trace.id.demo.enums.response.ErrorCode;

public class JwtException extends BaseCustomException {

    private final String token;

    public JwtException(String token, ErrorCode errorCode) {
        super(errorCode);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
