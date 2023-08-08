package com.mydevlog.exception;

public class WrongSigningInformation extends MydevlogException{

    private static final String MESSAGE = "아이디 / 비밀번호가 올바르지 않습니다.";

    public WrongSigningInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
