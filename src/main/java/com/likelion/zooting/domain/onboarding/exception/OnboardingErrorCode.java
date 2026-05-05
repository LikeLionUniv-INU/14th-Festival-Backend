package com.likelion.zooting.domain.onboarding.exception;

import com.likelion.zooting.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum OnboardingErrorCode implements BaseErrorCode {

  GENDER_REQUIRED("ONBOARDING_4001", "성별은 필수입니다.", HttpStatus.BAD_REQUEST),

  INTEREST_COUNT_INVALID("ONBOARDING_4002", "관심사는 정확히 3개 선택해야 합니다.", HttpStatus.BAD_REQUEST),
  MOVIE_GENRE_COUNT_INVALID("ONBOARDING_4003", "영화 장르는 정확히 2개 선택해야 합니다.", HttpStatus.BAD_REQUEST),
  ANIMAL_TYPE_COUNT_INVALID("ONBOARDING_4004", "선호 동물상은 정확히 3개 선택하거나 상관없음을 선택해야 합니다.", HttpStatus.BAD_REQUEST),

  ANIMAL_TYPE_NOT_FOUND("ONBOARDING_4005", "존재하지 않는 동물상입니다.", HttpStatus.BAD_REQUEST),
  INTEREST_NOT_FOUND("ONBOARDING_4006", "존재하지 않는 관심사입니다.", HttpStatus.BAD_REQUEST),
  MOVIE_GENRE_NOT_FOUND("ONBOARDING_4007", "존재하지 않는 영화 장르입니다.", HttpStatus.BAD_REQUEST),

  // 프로필 조회 시 온보딩 미완료 상태 대응
  ONBOARDING_NOT_FOUND("ONBOARDING_4041", "온보딩 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;

  OnboardingErrorCode(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  @Override
  public String getCode() { return code; }
  @Override
  public String getMessage() { return message; }
  @Override
  public HttpStatus getStatus() { return status; }
}