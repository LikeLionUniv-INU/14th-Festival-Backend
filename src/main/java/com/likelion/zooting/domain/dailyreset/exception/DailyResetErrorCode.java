package com.likelion.zooting.domain.dailyreset.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum DailyResetErrorCode implements BaseErrorCode {

    DAILY_RESET_DATA_NOT_FOUND("DAILY_RESET_4041", "초기화할 일일 운영 데이터가 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    DailyResetErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}