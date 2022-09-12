package com.csStudy.CardGame.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiErrorResponse> apiErrorExceptionHandler(RuntimeException ex) {
        ApiErrorException exception = (ApiErrorException) ex;

        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(exception.getErrorCode())
                .errorName(exception.getErrorName())
                .errorMessage(exception.getErrorMessage())
                .build();

        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> integrityExceptionHandler(RuntimeException ex) {
        ApiErrorException defaultException = ApiErrorException.createException(
                ApiErrorEnums.RESOURCE_CONFLICT,
                HttpStatus.CONFLICT,
                null,
                ex.getMessage()
        );

        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(defaultException.getErrorCode())
                .errorName(defaultException.getErrorName())
                .errorMessage(defaultException.getErrorMessage())
                .build();

        return new ResponseEntity<>(response, defaultException.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> accessDeniedExceptionHandler(RuntimeException ex) {
        ApiErrorException defaultException = ApiErrorException.createException(
                ApiErrorEnums.INVALID_ACCESS,
                HttpStatus.UNAUTHORIZED,
                null,
                ex.getMessage()
        );

        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(defaultException.getErrorCode())
                .errorName(defaultException.getErrorName())
                .errorMessage(defaultException.getErrorMessage())
                .build();

        return new ResponseEntity<>(response, defaultException.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> extraExceptionHandler(RuntimeException ex) {
        ApiErrorException defaultException = ApiErrorException.createException(
                ApiErrorEnums.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                ex.getMessage()
        );

        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(defaultException.getErrorCode())
                .errorName(defaultException.getErrorName())
                .errorMessage(defaultException.getErrorMessage())
                .build();

        return new ResponseEntity<>(response, defaultException.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("HttpMediaTypeNotSupportedException: 유효하지 않은 Content-Type");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.INVALID_REQUEST_BODY), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("MissingServletRequestPartException: Request Body Property 누락");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.INVALID_REQUEST_BODY), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("MissingServletRequestParameterException: Request Parameter Property 누락");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.INVALID_REQUEST_PARAMETER), status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("TypeMismatchException: 파라미터 타입 불일치");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.INVALID_PATH_VARIABLE), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("HttpRequestMethodNotSupportedException: 지원되지 않는 메소드 사용");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.INVALID_METHOD), status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("NoHandlerFoundException: 핸들러가 없는(존재하지 않는) API 호출");
        return new ResponseEntity<>(
                createErrorResponse(ApiErrorEnums.RESOURCE_NOT_FOUND), status);
    }

    private ApiErrorResponse createErrorResponse(ApiError apiError) {
        return ApiErrorResponse.builder()
                .errorCode(apiError.getErrorCode())
                .errorName(apiError.getErrorName())
                .errorMessage(apiError.getErrorMessage())
                .build();
    }
}
