package com.likelion.zooting.domain.dailyreset.controller;

import com.likelion.zooting.domain.dailyreset.dto.DailyResetResponse;
import com.likelion.zooting.domain.dailyreset.service.DailyResetService;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/daily-reset")
public class DailyResetController implements DailyResetControllerDocs {

    private final DailyResetService dailyResetService;

    @DeleteMapping
    public ApiResponse<DailyResetResponse> resetDailyData() {
        DailyResetResponse response = dailyResetService.resetDailyData();
        return ApiResponse.success(response);
    }
}
