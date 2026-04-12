package com.example.portfolio.global.error;

import com.example.portfolio.global.error.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 CustomException 처리 (핵심)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorResponse response = ErrorResponse.builder()
                .status(e.getStatus().value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getStatus()).body(response);
    }

    // 일반 RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}