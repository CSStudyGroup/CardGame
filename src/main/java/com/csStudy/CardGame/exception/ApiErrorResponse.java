package com.csStudy.CardGame.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiErrorResponse {
    private final int errorCode;
    private final String errorName;
    private final String errorMessage;
}
