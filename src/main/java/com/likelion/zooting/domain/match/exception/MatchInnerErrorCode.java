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
public enum MatchInnerErrorCode implements BaseErrorCode {
    MATCH_TIME_FORBIDDEN("MATCH_4032", "실제 매칭은 17시 이후에만 실행할 수 있습니다.", HttpStatus.FORBIDDEN),

    NO_MATCHED_USER_CANDIDATES("MATCH_4041", "매칭 대상 사용자가 없습니다.", HttpStatus.NOT_FOUND),

    DUPLICATE_EXECUTION_OF_MATCH_SAVE("MATCH_4091", "이미 매칭 결과가 저장되어 있습니다.", HttpStatus.CONFLICT),

    MALE_USER_ID_NOT_FOUND("MATCH_5001", "매칭 가능한 남성 사용자 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_ID_NOT_FOUND("MATCH_5002", "매칭 가능한 여성 사용자 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_PREFERRED_ANIMAL_ID_NOT_FOUND("MATCH_5003", "사용자의 선호 동물상 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_ANIMAL_MATCH_CANDIDATE_NOT_FOUND("MATCH_5004", "동물상 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_ANIMAL_MATCH_CANDIDATE_NOT_FOUND("MATCH_5005", "동물상 점수 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_INTEREST_ID_NOT_FOUND("MATCH_5006", "사용자의 관심사 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_INTEREST_MATCH_CANDIDATE_NOT_FOUND("MATCH_5007", "관심사 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_INTEREST_MATCH_CANDIDATE_NOT_FOUND("MATCH_5008", "관심사 점수 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_MOVIE_GENRE_ID_NOT_FOUND("MATCH_5009", "사용자의 영화 장르 ID가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALE_USER_MOVIE_GENRE_MATCH_CANDIDATE_NOT_FOUND("MATCH_50010", "영화 장르 점수 계산을 위한 매칭 대기 리스트에 남성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FEMALE_USER_MOVIE_GENRE_MATCH_CANDIDATE_NOT_FOUND("MATCH_50011", "영화 장르 계산을 위한 매칭 대기 리스트에 여성 사용자 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    DUPLICATE_ID("MATCH_50012", "중복된 ID가 존재합니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    MatchInnerErrorCode(String code, String message, HttpStatus status) {
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
