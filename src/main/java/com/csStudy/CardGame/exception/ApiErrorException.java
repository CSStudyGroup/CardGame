package com.csStudy.CardGame.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@Setter
public class ApiErrorException extends RuntimeException {
    private HttpStatus status;
    private String     errorName   ;
    private int        errorCode   ;
    private String     errorMessage;

    // constructors
    public ApiErrorException(ApiError error, HttpStatus status){
        this.status = status;
        this.errorCode = error.getErrorCode();
        this.errorName = error.getErrorName();
        this.errorMessage = error.getErrorMessage();
    }

    // raise utility functions
    public static ApiErrorException createException(ApiError error, HttpStatus status, String logMessage) {
        if (logMessage != null)
            log.info(logMessage);
        return new ApiErrorException(error, status);
    }
}
