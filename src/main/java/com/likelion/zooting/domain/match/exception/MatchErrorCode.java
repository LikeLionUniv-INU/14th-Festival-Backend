package com.likelion.zooting.domain.match.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

/**
 * matching 도메인에서 발생할 수 있는 에러를 반환하기 위해 사용되는 Enum
 * <p>
 * 에러 코드 : [에러가 발생한 도메인] + "_" + [고유 식별자]
 * 메시지 : 에러가 발생한 상황에 대한 상세 설명
 * HttpStatus : 반환할 HTTP 상태 코드
 */
public enum MatchErrorCode implements BaseErrorCode {

    MALE_USER_ID_NOT_FOUND("MATCH_001", "매칭 가능한 남성 사용자 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_ID_NOT_FOUND("MATCH_002", "매칭 가능한 여성 사용자 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_USER_ID("MATCH_003", "매칭에 중복된 사용자 ID가 존재합니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_ANIMAL_MATCH_CANDIDATE("MATCH_004", "동물상 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_ANIMAL_MATCH_CANDIDATE("MATCH_005", "동물상 점수 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_INTEREST_MATCH_CANDIDATE("MATCH_006", "관심사 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_INTEREST_MATCH_CANDIDATE("MATCH_007", "관심사 점수 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_MOVIE_GENRE_MATCH_CANDIDATE("MATCH_008", "영화 장르 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_MOVIE_GENRE_MATCH_CANDIDATE("MATCH_009", "영화 장르 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
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
