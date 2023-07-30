package com.mydevlog.controller;

import com.mydevlog.exception.MydevlogException;
import com.mydevlog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = ErrorResponse.builder().code("400").message("잘못된 요청입니다.").validation(new HashMap<>()).build();

        List<FieldError> fieldErrors = e.getFieldErrors();

        fieldErrors.stream().forEach(error -> {
            errorResponse.addValidation(error.getField(), error.getDefaultMessage());
        });
        return errorResponse;
    }

    @ExceptionHandler(MydevlogException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> postNotFound(MydevlogException e) {

        int statusCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(errorResponse);

        return response;
    }
}
