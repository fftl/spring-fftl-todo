package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.config.error.BusinessException;
import com.fftl.springfftltodo.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        log.warn("[BusinessException] {}", ex.getMessage());

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(ApiResponse.fail(ex.getErrorCode().getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[Validation] {}", errorMessage);

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(400, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAny(Exception ex) {
        log.error("[Unhandled Exception]", ex);

        return ResponseEntity
                .status(500)
                .body(ApiResponse.fail(500, "Internal server error"));
    }
}
