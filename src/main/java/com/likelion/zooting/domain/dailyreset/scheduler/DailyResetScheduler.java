package com.likelion.zooting.domain.dailyreset.scheduler;

import com.likelion.zooting.domain.dailyreset.service.DailyResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DailyResetScheduler {

    private final DailyResetService dailyResetService;

    @Scheduled(cron = "59 59 10 * * *", zone = "Asia/Seoul")
    public void resetDailyData() {
        LocalDate serviceDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        dailyResetService.archiveAndReset(serviceDate);
    }
}
