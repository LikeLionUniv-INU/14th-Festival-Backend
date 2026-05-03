package com.likelion.zooting.domain.onboarding.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record OnboardingRequest(
    @NotNull(message = "성별은 필수입니다")
    @Pattern(regexp = "male|female", message = "성별은 male 또는 female이어야 합니다")
    String gender,

    @NotBlank(message = "내 동물상은 필수입니다")
    String animalType,

    @NotNull
    @Size(min = 3, max = 3, message = "원하는 상대 동물상은 정확히 3개를 선택해야 합니다")
    List<String> preferredAnimals,

    @NotNull
    @Size(min = 3, max = 3, message = "관심사는 정확히 3개를 선택해야 합니다")
    List<String> interests,

    @NotNull
    @Size(min = 2, max = 2, message = "영화 장르는 정확히 2개를 선택해야 합니다")
    List<String> movieGenres
) {}
