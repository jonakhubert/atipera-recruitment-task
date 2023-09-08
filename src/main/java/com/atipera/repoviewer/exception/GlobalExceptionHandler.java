package com.atipera.repoviewer.exception;

import com.atipera.repoviewer.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        HttpMediaTypeNotAcceptableException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_ACCEPTABLE.value(),
            ex.getMessage()
        );

        return handleExceptionInternal(ex, apiError, setHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

//    @ExceptionHandler(HttpClientErrorException.NotFound.class)
//    public ResponseEntity<ApiError> handleUserNotFoundException(
//        HttpClientErrorException.NotFound ex,
//        HttpServletRequest request
//    ) {
//        ApiError apiError = new ApiError(
//            HttpStatus.NOT_FOUND.value(),
//            ex.getMessage()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
