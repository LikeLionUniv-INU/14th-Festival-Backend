package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.exception.MatchInnerErrorCode;
import com.likelion.zooting.domain.match.service.MatchService;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/match")
public class MatchController implements MatchControllerDocs {
    private final MatchService matchService;

    @Override
    public ResponseEntity<ApiResponse<MatchRequest>> simulateMatch() {
        // validateMatchTime(); // 시간 검증 로직 분리
        // 시뮬레이션이므로 저장은 하지 않음 (false)
        return ResponseEntity.ok(ApiResponse.success(matchService.getResultOfMatching(false)));
    }

    @Override
    public ResponseEntity<ApiResponse<MatchRequest>> runMatch() {
        validateMatchTime();
        // 실제 실행이므로 저장 (true)
        return ResponseEntity.ok(ApiResponse.success(matchService.getResultOfMatching(true)));
    }

    // 공통 검증 로직
    private void validateMatchTime() {
        // 서버의 현재 시간 (UTC)
        LocalTime serverNow = LocalTime.now();

        // 서버 시간 기준 08:00 ~ 15:00 사이가 아니라면 (= 한국 시간 17:00 ~ 24:00가 아니라면)
        if (serverNow.isBefore(LocalTime.of(8, 0)) || serverNow.isAfter(LocalTime.of(15, 0))) {
            throw new GeneralException(MatchInnerErrorCode.MATCH_TIME_FORBIDDEN);
        }
    }
}
