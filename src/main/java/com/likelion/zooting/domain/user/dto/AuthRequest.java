package com.likelion.zooting.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(

        @NotBlank(message = "인스타 아이디는 필수입니다.")
        String instagramId,

        @NotBlank(message = "본인확인 숫자는 필수입니다.")
        @Pattern(regexp = "\\d{4}", message = "본인확인 숫자는 4자리 숫자여야 합니다.")
        String verificationPin
) {
}