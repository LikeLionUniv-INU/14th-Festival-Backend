package com.likelion.zooting.global.exception.code;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    String getCode();
    String getMessage();
    HttpStatus getStatus();
}
