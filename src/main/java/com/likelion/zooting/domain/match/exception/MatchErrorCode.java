package com.likelion.zooting.domain.match.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

/**
 * matching 도메인에서 발생할 수 있는 에러를 반환하기 위해 사용되는 Enum
 * <p>
 * 에러 코드 : [에러가 발생한 도메인] + "_" + [에러코드] + [고유 식별자]
 * 메시지 : 에러가 발생한 상황에 대한 상세 설명
 * HttpStatus : 반환할 HTTP 상태 코드
 */
public enum MatchErrorCode implements BaseErrorCode {
    MATCH_TIME_FORBIDDEN("MATCH_4032", "실제 매칭은 17시 이후에만 실행할 수 있습니다.", HttpStatus.FORBIDDEN),

    NO_MATCHED_USER_CANDIDATES("MATCH_4041", "매칭 대상 사용자가 없습니다.", HttpStatus.NOT_FOUND),

    DUPLICATE_EXECUTION_OF_MATCH_SAVE("MATCH_4091", "이미 매칭 결과가 저장되어 있습니다.", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    MatchErrorCode(String code, String message, HttpStatus status) {
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
