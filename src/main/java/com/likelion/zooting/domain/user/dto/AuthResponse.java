package com.likelion.zooting.domain.user.dto;

public record AuthResponse(
        Long userId,
        String instagramId,
        boolean isComplete,
        boolean privacyConsent,
        String accessToken
) {
}