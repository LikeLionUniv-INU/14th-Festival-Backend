package com.likelion.zooting.domain.history.controller;

import com.likelion.zooting.domain.history.dto.HistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "History", description = "이력 관리 관련 API")
@RequestMapping("/api/internal/history")
public interface HistoryControllerDocs {

    @Operation(
            summary = "이력 생성",
            description = "현재 저장된 매칭 결과를 기반으로 <b>사용자별로</b> 이력을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이력 생성 성공",
                    content = @Content(schema = @Schema(implementation = HistoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "매칭 저장 미실행",
                    content = @Content(schema = @Schema(implementation = com.likelion.zooting.global.response.ApiResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "저장할 데이터(User)가 없음",
                    content = @Content(schema = @Schema(implementation = com.likelion.zooting.global.response.ApiResponse.class))
            )
    })
    @PostMapping("/create")
    ResponseEntity<com.likelion.zooting.global.response.ApiResponse<HistoryResponse>> createHistories();
}
