package com.likelion.zooting.domain.match.policy;

import com.likelion.zooting.domain.match.repository.data.TempMatchResult;

import java.util.Map;

/**
 * <h2>핵심 매칭 정책</h2>
 *
 * <p><b>[매칭 원칙]</b></p>
 * <ul>
 * <li><b>최우선 순위:</b> 합산 점수가 높은 쌍부터 우선적으로 매칭을 확정합니다.</li>
 * <li><b>필터링 조건:</b> 총 매칭 점수가 90점 이상인 것만 통과시킵니다.</li>
 * </ul>
 *
 * <p><b>[동점 발생 시 우선순위 처리]</b></p>
 * <ol>
 * <li><b>관심사 다수 일치:</b> 모든 가중치가 합산되어 {@code scoreBoard}에 반영되므로, 절대 점수 순위에 따라 자동으로 우선순위가 결정됩니다.</li>
 * <li><b>제출 시간순:</b> 점수가 동일할 경우, DB 조회 순서(조기 제출자 우선)를 유지하여 먼저 처리된 유저를 우선 매칭합니다.</li>
 * <li><b>무작위성:</b> 위 조건이 모두 동일할 경우, 시스템 내부의 데이터 추출 메커니즘에 따른 자연스러운 무작위성을 따릅니다.</li>
 * </ol>
 */
public interface MatchPolicy {
    /**
     * 그리디(Greedy) 알고리즘 기반의 매칭 시뮬레이션을 수행합니다.
     *
     * <p><b>[동작 과정]</b></p>
     * <ol>
     * <li><b>필터링:</b> 전체 점수판에서 90점을 초과하는 유효한 결과만을 선별합니다.</li>
     * <li><b>정렬:</b> 선별된 매칭 후보들을 점수가 높은 순(내림차순)으로 정렬합니다.</li>
     * <li><b>확정:</b> 정렬된 순서대로 매칭 그룹에 포함시키며, 매칭 쌍 중 한 명이라도 이미 매칭이 완료된 상태라면 해당 쌍은 제외합니다.</li>
     * </ol>
     *
     * @param scoreBoard          사용자 간 점수 인접 리스트
     * @param indexToMaleUserId   인덱스를 남성 사용자 ID로 매핑하는 Map
     * @param indexToFemaleUserId 인덱스를 여성 사용자 ID로 매핑하는 Map
     * @return 시뮬레이션 결과로 도출된 최종 매칭 리스트
     */
    TempMatchResult simulateMatching(int[][] scoreBoard, Map<Integer, Long> indexToMaleUserId, Map<Integer, Long> indexToFemaleUserId);
}
