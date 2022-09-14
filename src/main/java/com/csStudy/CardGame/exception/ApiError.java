package com.csStudy.CardGame.exception;

public interface ApiError {
    int getErrorCode();
    String getErrorName();
    String getErrorMessage();
}
