package com.likelion.zooting.domain.match.dto;

import com.likelion.zooting.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder    // 속성값이 많아 사용
public record MatchResDto(
        Integer totalUserCount,     // 매칭에 적용된 전체 사용자 수
        Integer matchedPairCount,   // 매칭된 짝의 개수
        Integer unmatchedUserCount, // 매칭에서 제외된 사용자 수
        LocalDateTime simulatedAt   // 매칭 시뮬레이션 돌린 시각
) {
}
