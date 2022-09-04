package com.csStudy.CardGame.exception;

import org.springframework.http.HttpStatus;

public interface ApiError {
    int getErrorCode();
    String getErrorName();
    String getErrorMessage();
}
