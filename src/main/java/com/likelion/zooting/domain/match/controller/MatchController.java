package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.exception.MatchInnerErrorCode;
import com.likelion.zooting.domain.match.service.MatchService;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
public class MatchController implements MatchControllerDocs {
    private final MatchService matchService;

    @Override
    public ResponseEntity<ApiResponse<MatchRequest>> simulateMatch() {
        validateMatchTime(); // 시간 검증 로직 분리
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
        if (LocalTime.now().isBefore(LocalTime.of(17, 0))) { // 17시 이전이면 예외 발생
            throw new GeneralException(MatchInnerErrorCode.MATCH_TIME_FORBIDDEN);
        }
    }
}
