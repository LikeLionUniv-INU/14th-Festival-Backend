package com.likelion.zooting.domain.dailyreset.service;

import com.likelion.zooting.domain.dailyreset.repository.DailyResetJdbcRepository;
import com.likelion.zooting.domain.dailyreset.repository.DailyArchiveJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DailyResetService {

    private final DailyArchiveJdbcRepository dailyArchiveJdbcRepository;
    private final DailyResetJdbcRepository dailyResetJdbcRepository;

    @Transactional
    public void archiveAndReset(LocalDate serviceDate) {
        LocalDateTime archivedAt = LocalDateTime.now();

        dailyArchiveJdbcRepository.archiveUsers(serviceDate, archivedAt);
        dailyArchiveJdbcRepository.archiveUserInterests(serviceDate, archivedAt);
        dailyArchiveJdbcRepository.archiveUserMovieGenres(serviceDate, archivedAt);
        dailyArchiveJdbcRepository.archiveUserAnimalTypes(serviceDate, archivedAt);
        dailyArchiveJdbcRepository.archiveMatches(serviceDate, archivedAt);

        dailyResetJdbcRepository.deleteLiveData();
    }
}
