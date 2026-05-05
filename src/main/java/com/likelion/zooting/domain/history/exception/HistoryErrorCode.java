package com.likelion.zooting.domain.history.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum HistoryErrorCode implements BaseErrorCode {
    BEFORE_EXECUTE_MATCH_SAVED("HISTORY_4031", "매칭 저장 미실행",  HttpStatus.FORBIDDEN),
    NO_USER_FOR_CREATE_HISTORY("HISTORY_4041", "저장할 데이터(User)가 없음",  HttpStatus.NOT_FOUND)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    HistoryErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }
}
