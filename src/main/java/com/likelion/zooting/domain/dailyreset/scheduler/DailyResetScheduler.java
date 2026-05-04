package com.likelion.zooting.domain.dailyreset.scheduler;

import com.likelion.zooting.domain.dailyreset.dto.DailyResetResponse;
import com.likelion.zooting.domain.dailyreset.service.DailyResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyResetScheduler {

    private final DailyResetService dailyResetService;

    @Scheduled(cron = "59 59 10 * * *", zone = "Asia/Seoul")
    public void resetDailyData() {
        log.info("[DailyResetScheduler] 일일 데이터 초기화를 시작합니다.");

        DailyResetResponse response = dailyResetService.resetDailyData();

        log.info(
                "[DailyResetScheduler] 일일 데이터 초기화 완료 - isDeleted: {}, deletedAt: {}, match: {}, userInterest: {}, userMovieGenre: {}, userPreferredAnimalType: {}, user: {}",
                response.isDeleted(),
                response.deletedAt(),
                response.deletedMatchCount(),
                response.deletedUserInterestCount(),
                response.deletedUserMovieGenreCount(),
                response.deletedUserPreferredAnimalTypeCount(),
                response.deletedUserCount()
        );
    }
}
