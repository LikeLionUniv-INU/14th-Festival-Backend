package com.likelion.zooting.domain.match.dto;

import com.likelion.zooting.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder    // 속성값이 많아 사용
@Schema(description = "매칭 실행 결과 응답 데이터")
public record MatchResDto(
        @Schema(description = "전체 참여 사용자 수")
        Integer totalUserCount,
        @Schema(description = "매칭된 짝(pair) 수")
        Integer matchedPairCount,
        @Schema(description = "매칭되지 않는 사용자 수")
        Integer unmatchedUserCount,
        @Schema(description = "시뮬레이션 실행 시간")
        LocalDateTime simulatedAt
) {
}
