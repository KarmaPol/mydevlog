package com.mydevlog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MydevlogException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();
    public MydevlogException(String message) {
        super(message);
    }

    public MydevlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
    public void addValidation(String fieldname, String message){
        validation.put(fieldname, message);
    }
}
