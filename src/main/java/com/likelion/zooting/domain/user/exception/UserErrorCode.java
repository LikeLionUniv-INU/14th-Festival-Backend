package com.likelion.zooting.domain.user.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements BaseErrorCode {

    INSTAGRAM_ID_REQUIRED("USER_4001", "인스타 아이디는 필수입니다.", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_PIN("USER_4002", "본인확인 숫자는 4자리 숫자여야 합니다.", HttpStatus.BAD_REQUEST),
    PRIVACY_CONSENT_REQUIRED("USER_4003", "개인정보 동의 여부는 필수입니다.", HttpStatus.BAD_REQUEST),

    PIN_MISMATCH("USER_4011", "본인확인 숫자가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    PRIVACY_CONSENT_DENIED("USER_4031", "개인정보 수집 및 이용에 동의해야 서비스를 이용할 수 있습니다.", HttpStatus.FORBIDDEN),

    USER_NOT_FOUND("USER_4041", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

    ALREADY_COMPLETED("USER_4091", "이미 설문을 완료한 사용자입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
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