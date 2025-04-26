package com.trace.id.demo.enums.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DEFAULT(500, "서버에서 예상치 못한 오류가 발생했습니다.", "Internal Server Error"),
    ;

    private final int errorCode;

    private final String errorKorMessage;

    private final String errorEngMessage;
}
