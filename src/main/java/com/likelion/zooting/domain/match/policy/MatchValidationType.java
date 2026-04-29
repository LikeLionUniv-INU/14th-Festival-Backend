package com.likelion.zooting.domain.match.policy;

import com.likelion.zooting.domain.match.exception.MatchInnerErrorCode;

/**
 * 데이터 정합성 검증 시, 해당 엔티티와 발생시킬 에러 코드를 매핑하는 Enum입니다.
 * <p>
 * {@link MatchInnerErrorCode}를 parameter로 사용합니다.
 * 검증 실패 시, 해당 상수에 정의된 {@link MatchInnerErrorCode}를 즉시 반환합니다.
 */
public enum MatchValidationType {
    MALE_USER_ID(MatchInnerErrorCode.MALE_USER_ID_NOT_FOUND),                               // 남성 사용자 ID 매칭 타입
    FEMALE_USER_ID(MatchInnerErrorCode.FEMALE_USER_ID_NOT_FOUND),                           // 여성 사용자 ID 매칭 타입
    PREFERRED_ANiMAL_ID(MatchInnerErrorCode.USER_PREFERRED_ANIMAL_ID_NOT_FOUND),                    // 선호 동물 ID 매칭 타입
    MALE_PREFERRED_ANiMAL_ID(MatchInnerErrorCode.MALE_USER_ANIMAL_MATCH_CANDIDATE_NOT_FOUND),       // 남성 선호 동물 ID 매칭 타입
    FEMALE_PREFERRED_ANiMAL_ID(MatchInnerErrorCode.FEMALE_USER_ANIMAL_MATCH_CANDIDATE_NOT_FOUND),   // 여성 선호 동물 ID 매칭 타입
    INTEREST_ID(MatchInnerErrorCode.USER_INTEREST_ID_NOT_FOUND),                            // 관심사 ID 매칭 타입
    MALE_INTEREST_ID(MatchInnerErrorCode.MALE_USER_INTEREST_MATCH_CANDIDATE_NOT_FOUND),     // 남성 관심사 ID 매칭 타입
    FEMALE_INTEREST_ID(MatchInnerErrorCode.FEMALE_USER_INTEREST_MATCH_CANDIDATE_NOT_FOUND), // 여성 관심사 ID 매칭 타입
    MOVIE_ID(MatchInnerErrorCode.USER_MOVIE_GENRE_ID_NOT_FOUND),                                    // 영화 장르 ID 매칭 타입
    MALE_MOVIE_ID(MatchInnerErrorCode.MALE_USER_MOVIE_GENRE_MATCH_CANDIDATE_NOT_FOUND),             // 남성 영화 장르 ID 매칭 타입
    FEMALE_MOVIE_ID(MatchInnerErrorCode.FEMALE_USER_MOVIE_GENRE_MATCH_CANDIDATE_NOT_FOUND),         // 여성 영화 장르 ID 매칭 타입
    DUPLICATED_ID(MatchInnerErrorCode.DUPLICATE_ID)  // 중복된 ID 정보
    ;
    private final MatchInnerErrorCode errorCode;

    MatchValidationType(MatchInnerErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public MatchInnerErrorCode getErrorCode() {
        return errorCode;
    }
}
