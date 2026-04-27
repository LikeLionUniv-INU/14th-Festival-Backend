package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.policy.MatchPolicy;
import com.likelion.zooting.domain.match.policy.MatchScoreCalculatePolicy;
import com.likelion.zooting.domain.match.policy.MatchScoreType;
import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.repository.data.TempMatchResult;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
public class MatchService {
    private final UserMatchRepository userMatchRepository;
    private final MatchPolicy matchPolicy;
    private final MatchScoreCalculatePolicy matchScoreCalculatePolicy;

    /**
     * 매칭 시뮬레이션을 수행하고 최종 결과를 반환합니다.
     *
     * <p><b>[설계 의도: 시뮬레이션과 저장 로직의 분리]</b></p>
     * 가벼운 시뮬레이션을 목적으로 설계되었으며, 영속화(Persistence)에 따른 오버헤드를 줄이기 위해
     * 저장 로직을 분리하여 구현하였습니다.
     *
     * <p><b>[수행 순서]</b></p>
     * <ol>
     * <li>{@code isSave} 파라미터를 통해 DB 저장 여부를 확인합니다.</li>
     * <li>그리디 알고리즘 기반의 매칭 시뮬레이션을 수행합니다.</li>
     * <li>저장 모드({@code isSave = true})일 경우, 시뮬레이션 결과를 바탕으로 최종 매칭 쌍을 DB에 영속화합니다.</li>
     * </ol>
     *
     * <p><b>[반환 데이터 성격]</b></p>
     * <ul>
     * <li><b>시뮬레이션 통계:</b> 전체 사용자 수, 성사된 매칭 쌍 개수, 미매칭 사용자 수</li>
     * <li><b>시간 기록:</b> 유저 ID 인덱싱 직후를 기준으로 한 매칭 시작 시간</li>
     * </ul>
     *
     * @param isSave DB 저장 여부 (true: 저장 수행, false: 시뮬레이션 결과만 반환)
     * @return 매칭 결과 통계 및 정보가 담긴 DTO
     */
    public MatchRequest getResultOfMatching(boolean isSave) {
        // 매칭 결과
        TempMatchResult simulatedMatchResult;
        LocalDateTime simulatedAt;

        // 남성 사용자 id와 여성 사용자 id에 대한 인덱스
        List<Long> maleUserIdList = userMatchRepository.findByGenderAndStatus(Gender.MALE, Status.SUBMITTED);
        List<Long> femaleUserIdList = userMatchRepository.findByGenderAndStatus(Gender.FEMALE, Status.SUBMITTED);

        checkValidate(maleUserIdList, MatchScoreType.MALE_USER_ID);     // DB에서 가져온 남성 사용자 id list 검정
        checkValidate(femaleUserIdList, MatchScoreType.FEMALE_USER_ID); // DB에서 가져온 여성 사용자 id list 검정

        Map<Long, Integer> maleUserIdIndex = IntStream.range(0, maleUserIdList.size()).boxed().collect(Collectors.toMap(i -> maleUserIdList.get(i), i -> i));
        Map<Long, Integer> femaleUserIdIndex = IntStream.range(0, femaleUserIdList.size()).boxed().collect(Collectors.toMap(i -> femaleUserIdList.get(i), i -> i));

        simulatedAt = LocalDateTime.now();  // 매칭 시작 시간

        // user간 점수 인접 리스트 초기화
        int[][] scoreBoard = new int[maleUserIdList.size()][femaleUserIdList.size()];

        // 점수 계산
        scoreBoard = matchScoreCalculatePolicy.calculateAnimalTypeScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = matchScoreCalculatePolicy.calculateInterestScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = matchScoreCalculatePolicy.calculateMovieGenreScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);

        // index(key) -> ID(value)로 바꾸기
        Map<Integer, Long> indexToMaleUserId = maleUserIdIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<Integer, Long> indexToFemaleUserId = femaleUserIdIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));

        // 매칭 (greedy algorithm)
        simulatedMatchResult = matchPolicy.simulateMatching(scoreBoard, indexToMaleUserId, indexToFemaleUserId);

        // 만일 매칭 결과 저장할 경우
        if (isSave) {
            // 추후 "매칭 결과 저장(확정) API" 구현 시 채울 부분
        }

        return MatchRequest.builder().totalUserCount(simulatedMatchResult.totalUserCount()).matchedPairCount(simulatedMatchResult.matchedPairCount()).unmatchedUserCount(simulatedMatchResult.unmatchedUserCount()).simulatedAt(simulatedAt).build();
    }

    /**
     * 매칭에 사용될 엔티티 리스트의 정합성을 검증합니다.
     *
     * <p><b>[검증 항목]</b></p>
     * <ul>
     * <li><b>도메인 범위:</b> 입력받은 리스트가 비어있거나 유효하지 않은지 확인합니다.</li>
     * <li><b>속성 누락:</b> 엔티티 내 필수 속성값이 누락되었는지 확인합니다.</li>
     * <li><b>중복성:</b> 리스트 내에 동일한 데이터가 중복으로 존재하는지 검사합니다.</li>
     * </ul>
     *
     * @param list 검사 대상 엔티티 리스트
     * @param type 검사 대상 엔티티 타입 (에러 메시지 처리용)
     * @throws GeneralException 데이터 정합성 검증 실패 시 발생
     */
    private <T> void checkValidate(List<T> list, MatchScoreType type) {
        // 1. list가 비어있는지 여부 확인
        if (list.isEmpty()) {
            throw new GeneralException(type.getErrorCode());
        }
        // 2. 속성 값 누락 검증
        for (T t : list) {
            if (t == null) {
                throw new GeneralException(type.getErrorCode());
            }
        }
        // 3. 중복 데이터 검사
        if (new HashSet<>(list).size() != list.size()) {
            throw new GeneralException(type.getErrorCode());
        }
    }
}
