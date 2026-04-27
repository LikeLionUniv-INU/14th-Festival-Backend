package com.likelion.zooting.domain.match.controller;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.service.MatchService;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MatchController implements MatchControllerDocs {
    private final MatchService matchService;

    @Override
    public ResponseEntity<ApiResponse<MatchRequest>> simulateMatch() {
        if (LocalTime.now().isAfter(LocalTime.of(17, 0))) {   // 17시(오후 5시)이후에 요청
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ApiResponse.failure("MATCH_4031", "테스트 매칭은 17시 이후에만 실행할 수 있습니다.", null));
        }

        MatchRequest matchResult = matchService.getResultOfMatching(false);
        if (Objects.isNull(matchResult)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.failure("MATCH_4041", "매칭 대상 사용자가 없습니다.", null));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponse.success(matchResult));
        }
    }
}
