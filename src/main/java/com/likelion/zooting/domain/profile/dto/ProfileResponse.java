package com.likelion.zooting.domain.profile.dto;

import lombok.Builder;

@Builder
public record ProfileResponse(
    String profileTag,    // 수식어 + 동물상 (예: "스포츠광 토끼")
    String animalType,    // 동물상 (예: "rabbit")
    String releaseTime,   // 18:00
    String releaseMessage // 매칭 안내 메시지
) {}