package com.atipera.repoviewer.exception;

import com.atipera.repoviewer.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(
        HttpClientErrorException.NotFound ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaTypeException(
        HttpMediaTypeNotAcceptableException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_ACCEPTABLE.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }
}
