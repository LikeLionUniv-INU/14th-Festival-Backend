package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.dto.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserMovieGenreMatchCandidate;
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
class MatchScoreCalculatePolicyImplTest {
    private final MatchScoreCalculatePolicyImpl matchScoreCalculatePolicy = new MatchScoreCalculatePolicyImpl();

    /**
     * <h2>테스트 데이터 명세: 동물상 매칭 (Animal Type)</h2>
     *
     * <p><b>[UserAnimalMatchCandidate 데이터 구조]</b></p>
     * <ul>
     * <li>{@code {Long userId, Long animalTypeId, Long preferredAnimalTypeId}}</li>
     * </ul>
     *
     * <p><b>[남성 후보 데이터]</b></p>
     * <ul>
     * <li><b>m1 :</b> {11L, 1L, 2L}, {11L, 1L, 8L}, {11L, 1L, 9L}</li>
     * <li><b>m2 :</b> {12L, 1L, 3L}, {12L, 1L, 7L}, {12L, 1L, 9L}</li>
     * <li><b>m3 :</b> {13L, 4L, 1L}, {13L, 4L, 8L}, {13L, 4L, 9L}</li>
     * </ul>
     *
     * <p><b>[여성 후보 데이터]</b></p>
     * <ul>
     * <li><b>f1 :</b> {23L, 3L, 1L}, {23L, 3L, 4L}, {23L, 3L, 5L}</li>
     * <li><b>f2 :</b> {21L, 8L, 1L}, {21L, 8L, 5L}, {21L, 8L, 6L}</li>
     * <li><b>f3 :</b> {22L, 9L, 4L}, {22L, 9L, 5L}, {22L, 9L, 6L}</li>
     * </ul>
     *
     * <p><b>[동물 타입 ID 분류]</b></p>
     * <ul>
     * <li><b>공통 ID :</b> 1, 2, 3</li>
     * <li><b>남성 전용 ID :</b> 4, 5, 6</li>
     * <li><b>여성 전용 ID :</b> 7, 8, 9</li>
     * </ul>
     */
    @Test
    void testCalculateAnimalTypeScore() {
        // 사용자의 동물상 및 선호 동물상 리스트 생성
        List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates = List.of(
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

        List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates = List.of(
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

        // 빈 점수판 생성
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        int[][] result = matchScoreCalculatePolicy.calculateAnimalTypeScore(
                scoreBoard,
                maleUserIdIndex,
                femaleUserIdIndex,
                maleUserAnimalMatchCandidates,
                femaleUserAnimalMatchCandidates);

        System.out.println(Arrays.deepToString(result));
    }

    /**
     * <h2>테스트 데이터 명세: 관심사 매칭 (Interests)</h2>
     *
     * <p><b>[UserInterestMatchCandidate 데이터 구조]</b></p>
     * <ul>
     * <li>{@code {Long userId, Long interestId}}</li>
     * </ul>
     *
     * <p><b>[남성 후보 데이터]</b></p>
     * <ul>
     * <li><b>m1 (11L) :</b> {2, 4, 8}</li>
     * <li><b>m2 (12L) :</b> {3, 6, 9}</li>
     * <li><b>m3 (13L) :</b> {1, 6, 8}</li>
     * </ul>
     *
     * <p><b>[여성 후보 데이터]</b></p>
     * <ul>
     * <li><b>f1 (23L) :</b> {1, 2, 3}</li>
     * <li><b>f2 (21L) :</b> {5, 7, 9}</li>
     * <li><b>f3 (22L) :</b> {2, 7, 8}</li>
     * </ul>
     *
     * <p><b>[관심사 ID 분류]</b></p>
     * <ul>
     * <li><b>전체 카테고리 :</b> 1, 2, 3, 4, 5, 6, 7, 8, 9</li>
     * </ul>
     */
    @Test
    void testCalculateInterestScore() {
        // 사용자의 관심사 리스트 생성 - 남성
        List<UserInterestMatchCandidate> maleUserInterestfMatchCandidates = List.of(
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
        List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates = List.of(
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

        // 빈 점수판 생성 -> 0인 값은 이전 값에서 거른 값이라 간주하기에, 100으로 채웠다.
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        // 채워진 점수판 생성(이전 testCalculateAnimalTypeScore의 result)
        int[][] accumulatedScoreBoard = {
                {0, 60, 0},
                {60, 0, 0},
                {0, 0, 60}
        };

        int[][] resultOfScoreBoard = matchScoreCalculatePolicy.calculateInterestScore(
                scoreBoard,
                maleUserIdIndex,
                femaleUserIdIndex,
                maleUserInterestfMatchCandidates,
                femaleUserInterestMatchCandidates);
        int[][] resultOfAccumulatedScoreBoard = matchScoreCalculatePolicy.calculateInterestScore(
                accumulatedScoreBoard,
                maleUserIdIndex,
                femaleUserIdIndex,
                maleUserInterestfMatchCandidates,
                femaleUserInterestMatchCandidates);
        System.out.println(Arrays.deepToString(resultOfScoreBoard));
        System.out.println(Arrays.deepToString(resultOfAccumulatedScoreBoard));
    }

    /**
     * <h2>테스트 데이터 명세: 영화 장르 매칭 (Movie Genre)</h2>
     *
     * <p><b>[UserMovieGenreMatchCandidate 데이터 구조]</b></p>
     * <ul>
     * <li>{@code {Long userId, Long movieGenreId}}</li>
     * </ul>
     *
     * <p><b>[남성 후보 데이터]</b></p>
     * <ul>
     * <li><b>m1 (11L) :</b> {1, 4}</li>
     * <li><b>m2 (12L) :</b> {3, 5}</li>
     * <li><b>m3 (13L) :</b> {2, 4}</li>
     * </ul>
     *
     * <p><b>[여성 후보 데이터]</b></p>
     * <ul>
     * <li><b>f1 (23L) :</b> {2, 4}</li>
     * <li><b>f2 (21L) :</b> {5, 6}</li>
     * <li><b>f3 (22L) :</b> {4, 5}</li>
     * </ul>
     *
     * <p><b>[영화 장르 ID 분류]</b></p>
     * <ul>
     * <li><b>전체 장르 카테고리 :</b> 1, 2, 3, 4, 5, 6</li>
     * </ul>
     */
    @Test
    void testCalculateMovieGenreScore() {
        // 사용자의 영화 장르 리스트 생성 - 남성
        List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidate = List.of(
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
        List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidate = List.of(
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

        // 빈 점수판 생성 -> 0인 값은 이전 값에서 거른 값이라 간주하기에, 100으로 채웠다.
        int[][] scoreBoard = new int[maleUserIdIndex.size()][femaleUserIdIndex.size()];

        // 채워진 점수판 생성(이전 testCalculateInterestScore의 result)
        int[][] accumulatedScoreBoard = {
                {30, 60, 60},
                {90, 30, 0},
                {30, 0, 90}
        };

        int[][] resultOfScoreBoard = matchScoreCalculatePolicy.calculateMovieGenreScore(
                scoreBoard,
                maleUserIdIndex,
                femaleUserIdIndex,
                maleUserMovieGenreMatchCandidate,
                femaleUserMovieGenreMatchCandidate);
        int[][] resultOfAccumulatedScoreBoard = matchScoreCalculatePolicy.calculateMovieGenreScore(
                accumulatedScoreBoard,
                maleUserIdIndex,
                femaleUserIdIndex,
                maleUserMovieGenreMatchCandidate,
                femaleUserMovieGenreMatchCandidate);
        System.out.println(Arrays.deepToString(resultOfScoreBoard));
        System.out.println(Arrays.deepToString(resultOfAccumulatedScoreBoard));
    }
}