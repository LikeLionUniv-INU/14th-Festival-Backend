package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.service.data.TempMatchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class MatchPolicyImplTest {
    private MatchPolicyImpl matchPolicyImpl = new MatchPolicyImpl();

    /**
     * <h2>테스트 기대 결과: 최종 점수판 (Scoreboard)</h2>
     *
     * <p><b>[최종 합산 점수 매트릭스]</b></p>
     * <table border="1">
     * <tr>
     * <th>구분</th>
     * <th>f1 (23L)</th>
     * <th>f2 (21L)</th>
     * <th>f3 (22L)</th>
     * </tr>
     * <tr>
     * <td><b>m1 (11L)</b></td>
     * <td>40점</td>
     * <td>60점</td>
     * <td>70점</td>
     * </tr>
     * <tr>
     * <td><b>m2 (12L)</b></td>
     * <td><mark>90점 (합격)</mark></td>
     * <td>40점</td>
     * <td>10점</td>
     * </tr>
     * <tr>
     * <td><b>m3 (13L)</b></td>
     * <td>50점</td>
     * <td>0점</td>
     * <td><mark>100점 (합격)</mark></td>
     * </tr>
     * </table>
     *
     * <p><b>[매칭 결과 요약]</b></p>
     * <ul>
     * <li><b>m2 - f1 :</b> 90점 (동물상 60 + 관심사 30 + 영화 0)</li>
     * <li><b>m3 - f3 :</b> 100점 (동물상 60 + 관심사 30 + 영화 10)</li>
     * <li>그 외 후보는 커트라인(90점) 미달로 매칭 제외</li>
     * </ul>
     */
    @Test
    void basicTestSimulateMatching(){
        // scoreBoard 생성
        int[][] scoreBoard = {
                {40, 60, 70},
                {90, 40, 10},
                {50, 0, 100}
        };

        // index(key) -> ID(value)로 바꾸기
        Map<Integer, Long> indexToMaleUserId = Map.of(0, 11L, 1, 12L, 2, 13L);
        Map<Integer, Long> indexToFemaleUserId = Map.of(0, 23L, 1, 21L, 2, 22L);

        TempMatchResult matchResult = matchPolicyImpl.simulateMatching(scoreBoard, indexToMaleUserId, indexToFemaleUserId);

        System.out.println("total : " + matchResult.totalUserCount());
        System.out.println("matched : " + matchResult.matchedPairCount());
        System.out.println("unmatched : "+matchResult.unmatchedUserCount());
        System.out.println("Pairs : \n" + matchResult.finalMatchedPairList());
    }

    /**
     * <h2>테스트 기대 결과: 한 사용자에 대해 여러 경쟁자가 등장할 경우</h2>
     * <p><b>[상황 설명]</b></p>
     * <ul>
     * <li>여성 <b>f1(23L)</b>은 한 명인데, 남성 <b>m1, m2, m3</b> 모두 f1과 90점 이상의 매칭 점수를 가짐.</li>
     * <li><b>m3(100점) > m2(95점) > m1(90점)</b> 순으로 점수가 높음.</li>
     * <li>시스템은 반드시 최고점자인 <b>m3-f1</b> 커플을 매칭하고, 나머지는 매칭되지 않아야 함.</li>
     * </ul>
     * <p><b>[최종 합산 점수 매트릭스]</b></p>
     * <table border="1">
     * <tr>
     * <th>구분</th>
     * <th>f1 (23L)</th>
     * <th>f2 (21L)</th>
     * <th>f3 (22L)</th>
     * </tr>
     * <tr>
     * <td><b>m1 (11L)</b></td>
     * <td>90점</td>
     * <td>0점</td>
     * <td>0점</td>
     * </tr>
     * <tr>
     * <td><b>m2 (12L)</b></td>
     * <td><mark>95점</mark></td>
     * <td>0점</td>
     * <td>0점</td>
     * </tr>
     * <tr>
     * <td><b>m3 (13L)</b></td>
     * <td>100점 (합격)</td>
     * <td>0점</td>
     * <td><mark>0점</mark></td>
     * </tr>
     * </table>
     * </ul>
     */
    @Test
    void duplicateUserTestSimulateMatching(){
        // scoreBoard 생성
        int[][] scoreBoard = {
                {90, 0, 0},
                {95, 0, 0},
                {100, 0, 0}
        };

        // index(key) -> ID(value)로 바꾸기
        Map<Integer, Long> indexToMaleUserId = Map.of(0, 11L, 1, 12L, 2, 13L);
        Map<Integer, Long> indexToFemaleUserId = Map.of(0, 23L, 1, 21L, 2, 22L);

        TempMatchResult matchResult = matchPolicyImpl.simulateMatching(scoreBoard, indexToMaleUserId, indexToFemaleUserId);

        System.out.println("total : " + matchResult.totalUserCount());
        System.out.println("matched : " + matchResult.matchedPairCount());
        System.out.println("unmatched : "+matchResult.unmatchedUserCount());
        System.out.println("Pairs : \n" + matchResult.finalMatchedPairList());
    }



}