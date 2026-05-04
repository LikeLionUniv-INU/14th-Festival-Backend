package com.likelion.zooting.domain.dailyreset.dto;

import java.time.LocalDateTime;

public record DailyResetResponse(
        boolean isDeleted,
        LocalDateTime deletedAt,
        long deletedMatchCount,
        long deletedUserInterestCount,
        long deletedUserMovieGenreCount,
        long deletedUserPreferredAnimalTypeCount,
        long deletedUserCount
) {
}
