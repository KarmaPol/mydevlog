package com.mydevlog.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends MydevlogException{

    private final static String MESSAGE = "잘못된 요청입니다.";

    private String fieldName;
    private String message;
    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public int getStatusCode(){
        return 400;
    }
}
