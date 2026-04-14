package com.likelion.zooting.global.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{

    private final BaseErrorCode errorCode;

    public GeneralException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
