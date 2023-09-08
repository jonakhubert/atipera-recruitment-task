package com.atipera.repoviewer.exception;

import com.atipera.repoviewer.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(
        HttpClientErrorException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "User not found."
        );

        return new ResponseEntity<>(apiError, setHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotAcceptableException(
        HttpMediaTypeNotAcceptableException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_ACCEPTABLE.value(),
            request.getHeader("Accept") + " not acceptable."
        );

        return new ResponseEntity<>(apiError, setHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
