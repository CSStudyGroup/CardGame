package com.csStudy.CardGame.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiErrorEnums implements ApiError{
    INVALID_REQUEST_PARAMETER(4000,"INVALID_REQUEST_PARAMETER","요청 파라미터 값이 유효하지 않습니다."),
    INVALID_REQUEST_BODY(4001, "INVALID_REQUEST_BODY", "본문 데이터 값이 유효하지 않습니다."),
    INVALID_PATH_VARIABLE(4002, "INVALID_PATH_VARIABLE", "경로 변수 값이 유효하지 않습니다."),
    RESOURCE_NOT_FOUND(4003, "RESOURCE_NOT_FOUND", "대상 API가 존재하지 않습니다."),
    INVALID_METHOD(4004, "INVALID_METHOD", "허용되지 않은 메소드입니다."),
    INVALID_EMAIL_OR_PASSWORD(4005, "INVALID_EMAIL_OR_PASSWORD", "등록되지 않은 이메일 또는 잘못된 패스워드입니다."),
    EXPIRED_TOKEN(4006, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    INVALID_TOKEN(4007, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    INTERNAL_SERVER_ERROR(5000,"INTERNAL_SERVER_ERROR","서버 에러입니다.");

    private final int errorCode;
    private final String errorName;
    private final String errorMessage;
}
