package com.likelion.zooting.domain.match.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

/**
 * matching 도메인에서 발생할 수 있는 에러를 반환하기 위해 사용되는 Enum
 */
public enum MatchErrorCode implements BaseErrorCode {
    MATCH_TIME_FORBIDDEN("MATCH_4032", "실제 매칭은 17시 이후에만 실행할 수 있습니다.", HttpStatus.FORBIDDEN),

    NO_MATCHED_USER_CANDIDATES("MATCH_4041", "매칭 대상 사용자가 없습니다.", HttpStatus.NOT_FOUND),
    MATCH_RESULT_NOT_FOUND("MATCH_4041", "매칭 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EXECUTION_OF_MATCH_SAVE("MATCH_4091", "이미 매칭 결과가 저장되어 있습니다.", HttpStatus.CONFLICT),

    // [추가] 신규 에러 코드 (생성자 순서: code, message, status)
    USER_4001("USER_4001", "인스타 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    USER_4002("USER_4002", "본인확인용 숫자는 숫자 4자리여야 합니다.", HttpStatus.BAD_REQUEST),
    AUTH_4011("AUTH_4011", "본인확인 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    MATCH_4031("MATCH_4031", "매칭 결과는 18시 이후에 확인할 수 있습니다.", HttpStatus.FORBIDDEN),
    MATCH_4041("MATCH_4041", "매칭 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
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
