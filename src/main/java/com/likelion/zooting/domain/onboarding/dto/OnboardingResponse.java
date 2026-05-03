package com.likelion.zooting.domain.onboarding.dto;

/**
 * 성공 시 ApiResponse의 'result' 필드에 담길 데이터입니다.
 * JSON 예시: { "profileCreated": true }
 */
public record OnboardingResponse(
    boolean isComplete
) {}
