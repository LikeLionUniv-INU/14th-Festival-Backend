package com.likelion.zooting.domain.match.policy;

import com.likelion.zooting.domain.match.exception.MatchInnerErrorCode;

/**
 * 데이터 정합성 검증 시, 해당 엔티티와 발생시킬 에러 코드를 매핑하는 Enum입니다.
 * <p>
 * {@link MatchInnerErrorCode}를 parameter로 사용합니다.
 * 검증 실패 시, 해당 상수에 정의된 {@link MatchInnerErrorCode}를 즉시 반환합니다.
 */
public enum MatchScoreType {
    MALE_USER_ID(MatchInnerErrorCode.MALE_USER_ID_NOT_FOUND),        // 남성 사용자 ID 매칭 타입
    FEMALE_USER_ID(MatchInnerErrorCode.FEMALE_USER_ID_NOT_FOUND),    // 여성 사용자 ID 매칭 타입
    ;
    private final MatchInnerErrorCode errorCode;

    MatchScoreType(MatchInnerErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public MatchInnerErrorCode getErrorCode() {
        return errorCode;
    }
}
