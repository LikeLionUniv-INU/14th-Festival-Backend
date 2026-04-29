package com.likelion.zooting.domain.match.policy;

import com.likelion.zooting.domain.match.dto.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserMovieGenreMatchCandidate;

import java.util.List;
import java.util.Map;

/**
 * <h2>매칭 점수 산정 및 필터링 정책</h2>
 *
 * <p><b>1. 점수 합산 기준 (최대 100점)</b></p>
 * <table border="1">
 * <tr><th>항목</th><th>산정 방식</th><th>최대 점수</th></tr>
 * <tr><td>동물상</td><td>이분적 처리 (하나라도 일치 시 60점, 미일치 시 0점)</td><td>60점</td></tr>
 * <tr><td>관심사</td><td>개별 관심사당 30점 부여</td><td>90점</td></tr>
 * <tr><td>영화 장르</td><td>개별 장르당 10점 부여</td><td>20점</td></tr>
 * </table>
 *
 * <p><b>2. 동물상 매칭 세부 규칙 (단방향성)</b></p>
 * <ul>
 * <li><b>특성:</b> 양방향 일치가 아닌, 본인의 동물상과 상대방의 선호 동물상을 대조하는 단방향 로직입니다.</li>
 * <li><b>공통 타입:</b> 강아지, 고양이, 햄스터</li>
 * <li><b>남성 전용:</b> 곰, 원숭이, 공룡</li>
 * <li><b>여성 전용:</b> 토끼, 사슴, 병아리</li>
 * </ul>
 *
 * <p><b>3. 확장성 및 정책 적용 원칙</b></p>
 * <ul>
 * <li><b>점수 누적 방식:</b> 각 정책은 독립적인 가중치를 가지며, {@code +=} 연산을 통해 최종 점수에 누적 합산됩니다.</li>
 * <li><b>전략적 배치:</b> 연산 효율성을 위해 필터링 강도가 높거나 우선순위가 높은 정책을 먼저 배치하는 것을 권장합니다.</li>
 * <li><b>배치 순서:</b> 동물상 -> 관심사 -> 영화 장르</li>
 * </ul>
 */
public interface MatchScoreCalculatePolicy {
    /**
     * 동물상 일치 여부에 따른 매칭 점수를 계산합니다.
     *
     * <p><b>[점수 산정 로직]</b></p>
     * <ul>
     * <li>점수는 60점 또는 0점으로 이분 처리하며, 하나라도 일치할 경우 만점을 부여합니다.</li>
     * </ul>
     *
     * <p><b>[데이터 무결성 및 일관성 처리]</b></p>
     * <ul>
     * <li><b>Case 1 (데이터 신규 추가):</b> 인덱스 생성 시점 이후 추가된 데이터는 Map에서 null을 반환하므로 continue로 제외하여 일관성을 유지합니다.</li>
     * <li><b>Case 2 (데이터 수정):</b> 속성값의 null 여부를 체크하여 생성 시점 이후의 수정 사항으로 인한 런타임 에러를 방지합니다.</li>
     * <li><b>Case 3 (점수의 원자성 유지):</b> 해당 정책은 이분적(Binary) 구조를 가집니다. 조건 충족 여부에 따라 점수는 중첩되지 않고 0점 혹은 60점 중 하나의 값만을 가집니다.</li>
     * </ul>
     *
     * @param scoreBoard        사용자 간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스 Map
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스 Map
     * @return 동물상 점수가 반영된 scoreBoard
     */
    int[][] calculateAnimalTypeScore(int[][] scoreBoard,
                                     Map<Long, Integer> maleUserIdIndex,
                                     Map<Long, Integer> femaleUserIdIndex,
                                     List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates,
                                     List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates);

    /**
     * 공통 관심사 일치 여부에 따른 매칭 점수를 계산합니다.
     *
     * <p><b>[점수 산정 로직]</b></p>
     * <ul>
     * <li>개별 관심사당 30점씩 부여하며, 최대 3개의 관심사(총 90점)를 가집니다.</li>
     * </ul>
     *
     * <p><b>[기술적 고려 사항 - 검증 로직 보류]</b></p>
     * <ul>
     * <li>현재 로직은 사용자가 관심사를 정확히 3개 선택했는지 검증하지 않습니다.</li>
     * <li>성별 그룹화 및 JOIN을 통한 DB 레벨의 검증을 기획했으나, JPQL의 서브쿼리 제약으로 인해 해당 구현은 보류되었습니다.</li>
     * </ul>
     *
     * <p><b>[데이터 무결성 및 일관성 처리]</b></p>
     * <ul>
     * <li><b>Case 1 (신규 데이터):</b> 인덱스 생성 시점 이후 추가된 데이터는 Map 조회 시 null을 반환하므로 {@code continue} 처리하여 일관성을 유지합니다.</li>
     * <li><b>Case 2 (수정 데이터):</b> 속성값의 null 체크를 통해 생성 시점 이후 발생할 수 있는 데이터 변경에 대처합니다.</li>
     * </ul>
     *
     * @param scoreBoard        사용자 간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스 Map
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스 Map
     * @return 관심사 점수가 반영된 scoreBoard
     */
    int[][] calculateInterestScore(int[][] scoreBoard,
                                   Map<Long, Integer> maleUserIdIndex,
                                   Map<Long, Integer> femaleUserIdIndex,
                                   List<UserInterestMatchCandidate> maleUserInterestMatchCandidates,
                                   List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates);

    /**
     * 선호 영화 장르 일치 여부에 따른 매칭 점수를 계산합니다.
     *
     * <p><b>[점수 산정 로직]</b></p>
     * <ul>
     * <li>장르당 10점씩 부여하며, 최대 2개의 장르(총 20점)를 가집니다.</li>
     * </ul>
     *
     * <p><b>[참조 사항]</b></p>
     * 데이터 검증 보류 사유는 {@link #calculateInterestScore}의 기술적 제약 사항과 동일합니다.
     *
     * <p><b>[데이터 무결성 및 일관성 처리]</b></p>
     * <ul>
     * <li><b>인덱스 기준 일관성:</b> 인덱스에 존재하지 않는 사용자 데이터는 {@code continue}로 제외하여 처리 시점의 정합성을 보장합니다.</li>
     * <li><b>런타임 안정성:</b> 각 속성값의 null 여부를 확인하여 데이터 수정으로 인한 오류를 방지합니다.</li>
     * </ul>
     *
     * @param scoreBoard        사용자 간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스 Map
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스 Map
     * @return 영화 장르 점수가 반영된 scoreBoard
     */
    int[][] calculateMovieGenreScore(int[][] scoreBoard,
                                     Map<Long, Integer> maleUserIdIndex,
                                     Map<Long, Integer> femaleUserIdIndex,
                                     List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates,
                                     List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates);
}
