package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchPolicyImplTest {
    @Mock
    private UserMatchRepository userMatchRepository;
    @InjectMocks
    private MatchScoreCaculatePolicyImpl matchScoreCaculatePolicy;

    /**
     * - UserAnimalMatchCandidate {Long userId, Long AnimalTypeId, Long preferredAnimalTypeId}
     * 남성
     * m1 : {11L, 1L, 2L}, {11L, 1L, 8L}, {11L, 1L, 9L}
     * m2 : {12L, 1L, 3L}, {12L, 1L, 7L}, {12L, 1L, 9L}
     * m3 : {13L, 4L, 1L}, {13L, 4L, 8L}, {13L, 4L, 9L}
     * 여성
     * f1 : {23L, 3L, 1L}, {23L, 3L, 4L}, {23L, 3L, 5L}
     * f2 : {21L, 8L, 1L}, {21L, 8L, 5L}, {21L, 8L, 6L}
     * f3 : {22L, 9L, 4L}, {22L, 9L, 5L}, {22L, 9L, 6L}
     * - 동물 타입 ID
     * 공통 ID : 1, 2, 3
     * 남성 ID : 4, 5, 6
     * 여성 ID : 7, 8, 9
     */
    @Test
    void testCalculateAnimalTypeScore() {
        // 사용자의 동물상 및 선호 동물상 리스트 생성
        List<UserAnimalMatchCandidate> maleUser = List.of(
                new UserAnimalMatchCandidate(11L, 1L, 2L), // m1
                new UserAnimalMatchCandidate(11L, 1L, 8L),
                new UserAnimalMatchCandidate(11L, 1L, 9L),
                new UserAnimalMatchCandidate(12L, 1L, 3L), // m2
                new UserAnimalMatchCandidate(12L, 1L, 7L),
                new UserAnimalMatchCandidate(12L, 1L, 9L),
                new UserAnimalMatchCandidate(13L, 4L, 1L), // m3
                new UserAnimalMatchCandidate(13L, 4L, 8L),
                new UserAnimalMatchCandidate(13L, 4L, 9L)
        );

        List<UserAnimalMatchCandidate> femaleUser = List.of(
                new UserAnimalMatchCandidate(23L, 3L, 1L), // f1
                new UserAnimalMatchCandidate(23L, 3L, 4L),
                new UserAnimalMatchCandidate(23L, 3L, 5L),
                new UserAnimalMatchCandidate(21L, 8L, 1L), // f2
                new UserAnimalMatchCandidate(21L, 8L, 5L),
                new UserAnimalMatchCandidate(21L, 8L, 6L),
                new UserAnimalMatchCandidate(22L, 9L, 4L), // f3
                new UserAnimalMatchCandidate(22L, 9L, 5L),
                new UserAnimalMatchCandidate(22L, 9L, 6L)
        );

        // 사용자 인덱스 생성
        Map<Long, Integer> maleUserIdIndex = Map.of(11L, 0, 12L, 1, 13L, 2);
        Map<Long, Integer> femaleUserIdIndex = Map.of(23L, 0, 21L, 1, 22L, 2);

        // userMatchRepository가 호출될 때 반환할 값 설정
        when(userMatchRepository.findUserAnimalMatchCandidates(Gender.MALE, Status.SUBMITTED)).thenReturn(maleUser);
        when(userMatchRepository.findUserAnimalMatchCandidates(Gender.FEMALE, Status.SUBMITTED)).thenReturn(femaleUser);

        // 빈 점수판 생성
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        int[][] result = matchScoreCaculatePolicy.calculateAnimalTypeScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);

        System.out.println(Arrays.deepToString(result));
    }

    /**
     * - UserInterestMatchCandidates {Long useId, Long interestId}
     * 남성
     * m1 : {11L, 2L}, {11L, 4L}, {11L, 8L}
     * m2 : {12L, 3L}, {12L, 6L}, {12L, 9L}
     * m3 : {13L, 1L}, {13L, 6L}, {13L, 8L}
     * 여성
     * f1 : {23L, 1L}, {23L, 2L}, {23L, 3L}
     * f2 : {21L, 5L}, {21L, 7L}, {21L, 9L},
     * f3 : {22L, 2L}, {22L, 7L}, {22L, 8L},
     * - 관심사 ID
     * 1, 2, 3, 4, 5, 6, 7, 8, 9
     */
    @Test
    void testCalculateInterestScore() {
        // 사용자의 관심사 리스트 생성 - 남성
        List<UserInterestMatchCandidate> maleUserInterest = List.of(
                // m1 (11L): 2, 4, 8
                new UserInterestMatchCandidate(11L, 2L),
                new UserInterestMatchCandidate(11L, 4L),
                new UserInterestMatchCandidate(11L, 8L),

                // m2 (12L): 3, 6, 9
                new UserInterestMatchCandidate(12L, 3L),
                new UserInterestMatchCandidate(12L, 6L),
                new UserInterestMatchCandidate(12L, 9L),

                // m3 (13L): 1, 6, 8
                new UserInterestMatchCandidate(13L, 1L),
                new UserInterestMatchCandidate(13L, 6L),
                new UserInterestMatchCandidate(13L, 8L)
        );

        // 사용자의 관심사 리스트 생성 - 여성
        List<UserInterestMatchCandidate> femaleUserInterest = List.of(
                // f1 (23L): 1, 2, 3
                new UserInterestMatchCandidate(23L, 1L),
                new UserInterestMatchCandidate(23L, 2L),
                new UserInterestMatchCandidate(23L, 3L),

                // f2 (21L): 5, 7, 9
                new UserInterestMatchCandidate(21L, 5L),
                new UserInterestMatchCandidate(21L, 7L),
                new UserInterestMatchCandidate(21L, 9L),

                // f3 (22L): 2, 7, 8
                new UserInterestMatchCandidate(22L, 2L),
                new UserInterestMatchCandidate(22L, 7L),
                new UserInterestMatchCandidate(22L, 8L)
        );

        // 사용자 인덱스 생성
        Map<Long, Integer> maleUserIdIndex = Map.of(11L, 0, 12L, 1, 13L, 2);
        Map<Long, Integer> femaleUserIdIndex = Map.of(23L, 0, 21L, 1, 22L, 2);

        // userMatchRepository가 호출될 때 반환할 값 설정
        when(userMatchRepository.findUserInterestMatchCandidates(Gender.MALE, Status.SUBMITTED)).thenReturn(maleUserInterest);
        when(userMatchRepository.findUserInterestMatchCandidates(Gender.FEMALE, Status.SUBMITTED)).thenReturn(femaleUserInterest);

        // 빈 점수판 생성 -> 0인 값은 이전 값에서 거른 값이라 간주하기에, 100으로 채웠다.
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        // 채워진 점수판 생성(이전 testCalculateAnimalTypeScore의 result)
        int[][] accumulatedScoreBoard = {
                {0, 60, 0},
                {60, 0, 0},
                {0, 0, 60}
        };

        int[][] resultOfScoreBoard = matchScoreCaculatePolicy.calculateInterestScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        int[][] resultOfAccumulatedScoreBoard = matchScoreCaculatePolicy.calculateInterestScore(accumulatedScoreBoard, maleUserIdIndex, femaleUserIdIndex);
        System.out.println(Arrays.deepToString(resultOfScoreBoard));
        System.out.println(Arrays.deepToString(resultOfAccumulatedScoreBoard));
    }

    /**
     * - UserMovieGenreMatchCandidate {Long userId, Long movieGenreId}
     * 남성
     * m1 : {11L, 1L}, {11L, 4L}
     * m2 : {12L, 3L}, {12L, 5L}
     * m3 : {13L, 2L}, {13L, 4L}
     * 여성
     * f1 : {23L, 2L}, {23L, 4L}
     * f2 : {21L, 5L}, {21L, 6L}
     * f3 : {22L, 4L}, {22L, 5L}
     * - 영화장르 ID
     * 1, 2, 3, 4, 5, 6
     */
    @Test
    void testCalculateMovieGenreScore() {
        // 사용자의 영화 장르 리스트 생성 - 남성
        List<UserMovieGenreMatchCandidate> maleUserMovieGenre = List.of(
                // m1 (11L): 1, 4
                new UserMovieGenreMatchCandidate(11L, 1L),
                new UserMovieGenreMatchCandidate(11L, 4L),

                // m2 (12L): 3, 5
                new UserMovieGenreMatchCandidate(12L, 3L),
                new UserMovieGenreMatchCandidate(12L, 5L),

                // m3 (13L): 2, 4
                new UserMovieGenreMatchCandidate(13L, 2L),
                new UserMovieGenreMatchCandidate(13L, 4L)
        );
        // 사용자의 영화 장르 리스트 생성 - 여성
        List<UserMovieGenreMatchCandidate> femaleUserMovieGenre = List.of(
                // f1 (23L): 2, 4
                new UserMovieGenreMatchCandidate(23L, 2L),
                new UserMovieGenreMatchCandidate(23L, 4L),

                // f2 (21L): 5, 6
                new UserMovieGenreMatchCandidate(21L, 5L),
                new UserMovieGenreMatchCandidate(21L, 6L),

                // f3 (22L): 4, 5
                new UserMovieGenreMatchCandidate(22L, 4L),
                new UserMovieGenreMatchCandidate(22L, 5L)
        );

        // 사용자 인덱스 생성
        Map<Long, Integer> maleUserIdIndex = Map.of(11L, 0, 12L, 1, 13L, 2);
        Map<Long, Integer> femaleUserIdIndex = Map.of(23L, 0, 21L, 1, 22L, 2);

        // userMatchRepository가 호출될 때 반환할 값 설정
        when(userMatchRepository.findUserMovieGenreMatchCandidate(Gender.MALE, Status.SUBMITTED)).thenReturn(maleUserMovieGenre);
        when(userMatchRepository.findUserMovieGenreMatchCandidate(Gender.FEMALE, Status.SUBMITTED)).thenReturn(femaleUserMovieGenre);

        // 빈 점수판 생성 -> 0인 값은 이전 값에서 거른 값이라 간주하기에, 100으로 채웠다.
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        // 채워진 점수판 생성(이전 testCalculateInterestScore의 result)
        int[][] accumulatedScoreBoard = {
                {30, 60, 60},
                {90, 30, 0},
                {30, 0, 90}
        };

        int[][] resultOfScoreBoard = matchScoreCaculatePolicy.calculateMovieGenreScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        int[][] resultOfAccumulatedScoreBoard = matchScoreCaculatePolicy.calculateMovieGenreScore(accumulatedScoreBoard, maleUserIdIndex, femaleUserIdIndex);
        System.out.println(Arrays.deepToString(resultOfScoreBoard));
        System.out.println(Arrays.deepToString(resultOfAccumulatedScoreBoard));
    }
}