package com.likelion.zooting.domain.history.controller;

import com.likelion.zooting.domain.history.dto.HistoryResponse;
import com.likelion.zooting.domain.history.exception.HistoryErrorCode;
import com.likelion.zooting.domain.history.sevice.HistoryService;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/history")
public class HistoryController implements HistoryControllerDocs {
    private final HistoryService historyService;
    @Override
    public ResponseEntity<ApiResponse<HistoryResponse>> createHistories() {
        HistoryResponse historyResponse = historyService.createHistories();
        // 1. 유저 데이터가 시스템에 아예 없는 경우 (404)
        if(historyResponse.totalUserCount() <= 0){
            throw new GeneralException(HistoryErrorCode.NO_USER_FOR_CREATE_HISTORY);
        }
        // 2. 매칭 결과(Matches)가 없어 이력이 생성되지 않은 경우 (403)
        if(historyResponse.matchedUserCount() <= 0){
            throw new GeneralException(HistoryErrorCode.BEFORE_EXECUTE_MATCH_SAVED);
        }
        return ResponseEntity.ok(ApiResponse.success(historyResponse));
    }
}