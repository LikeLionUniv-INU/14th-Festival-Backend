package com.likelion.zooting.domain.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record PrivacyRequest(

        @NotBlank(message = "인스타 ID는 필수입니다.")
        String instagramId,

        @AssertTrue(message = "개인정보 수집 및 이용 동의는 필수입니다.")
        Boolean privacyConsent
) {
}