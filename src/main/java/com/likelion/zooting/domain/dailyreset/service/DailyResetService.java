package com.likelion.zooting.domain.dailyreset.service;

import com.likelion.zooting.domain.dailyreset.dto.DailyResetResponse;
import com.likelion.zooting.domain.dailyreset.exception.DailyResetErrorCode;
import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.domain.usermoviegenre.repository.UserMovieGenreRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.repository.UserPreferredAnimalTypeRepository;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DailyResetService {

    private final MatchRepository matchRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserMovieGenreRepository userMovieGenreRepository;
    private final UserPreferredAnimalTypeRepository userPreferredAnimalTypeRepository;
    private final UserRepository userRepository;

    @Transactional
    public DailyResetResponse resetDailyData() {
        long matchCount = matchRepository.count();
        long userInterestCount = userInterestRepository.count();
        long userMovieGenreCount = userMovieGenreRepository.count();
        long userPreferredAnimalTypeCount = userPreferredAnimalTypeRepository.count();
        long userCount = userRepository.count();

        if (matchCount == 0
                && userInterestCount == 0
                && userMovieGenreCount == 0
                && userPreferredAnimalTypeCount == 0
                && userCount == 0) {
            throw new GeneralException(DailyResetErrorCode.DAILY_RESET_DATA_NOT_FOUND);
        }

        // FK 제약 조건을 고려하여 User를 참조하는 테이블부터 삭제
        matchRepository.deleteAllInBatch();

        userInterestRepository.deleteAllInBatch();
        userMovieGenreRepository.deleteAllInBatch();
        userPreferredAnimalTypeRepository.deleteAllInBatch();

        userRepository.deleteAllInBatch();

        return new DailyResetResponse(
                true,
                LocalDateTime.now(),
                matchCount,
                userInterestCount,
                userMovieGenreCount,
                userPreferredAnimalTypeCount,
                userCount
        );
    }
}
