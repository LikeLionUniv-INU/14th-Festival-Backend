package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Match", description = "매칭 관련 API")
@RequestMapping("/api/internal/match")
public interface MatchControllerDocs {

    @Operation(
            summary = "매칭 시뮬레이션",
            description = "현재 제출된 데이터를 기반으로 매칭을 가상 실행하며, <b>DB에 저장하지 않고</b> 결과 통계만 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "매칭 실행 성공",
                    content = @Content(schema = @Schema(implementation = MatchRequest.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "접근 제한 (17시 이전 호출)",
                    content = @Content(schema = @Schema(implementation = com.likelion.zooting.global.response.ApiResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "매칭 대상자 없음",
                    content = @Content(schema = @Schema(implementation = com.likelion.zooting.global.response.ApiResponse.class))
            )
    })
    @PostMapping("/simulate")
    ResponseEntity<com.likelion.zooting.global.response.ApiResponse<MatchRequest>> simulateMatch();
}
