package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.service.MatchService;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

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
 * - 동물 타입 AnimalType
 * 공통 ID : 1, 2, 3
 * 남성 ID : 4, 5, 6
 * 여성 ID : 7, 8, 9
 */
@ExtendWith(MockitoExtension.class)
class MatchPolicyImplTest {
    @Mock
    private UserMatchRepository userMatchRepository;
    @InjectMocks
    private MatchScoreCaculatePolicyImpl matchScoreCaculatePolicy;

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

}