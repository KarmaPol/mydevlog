package com.mydevlog.controller;

import com.mydevlog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = new ErrorResponse("400", "잘못된 요청입니다.");

        List<FieldError> fieldErrors = e.getFieldErrors();

        fieldErrors.stream().forEach(error -> {
            errorResponse.addValidation(error.getField(), error.getDefaultMessage());
        });
        return errorResponse;
    }
}
