package com.likelion.zooting.domain.history.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "이력 저장 결과")
public record HistoryResponse(
        @Schema(description = "이력 실행 성공 여부")
        boolean isComplete,
        @Schema(description = "전체 참여 사용자 수")
        Integer totalUserCount,
        @Schema(description = "매칭된 상태의 사용자 수")
        Integer matchedUserCount,
        @Schema(description = "매칭되지 않는 상태의 사용자 수")
        Integer unmatchedUserCount,
        @Schema(description = "에러 처리된 사용자 수")
        Integer erredUserCount,
        @Schema(description = "이력 저장한 시간")
        LocalDateTime simulatedAt
) {
}
