package com.likelion.zooting.domain.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MatchResultRequest(

        @NotBlank(message = "인스타 ID는 필수입니다.")
        String instagramId,

        @NotBlank(message = "본인확인용 숫자는 필수입니다.")
        @Pattern(regexp = "\\d{4}", message = "본인확인용 숫자는 숫자 4자리여야 합니다.")
        String verificationPin
) {
}