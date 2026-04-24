package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.dto.MatchResDto;
import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.repository.data.*;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserMatchRepository userMatchRepository;

    /**
     * getResultOfMatch
     *
     * <p>
     * 매칭 결과를 List로 담아 보냅니다.
     * <p>
     * 시뮬레이션 로직과 저장 로직의 분리
     * 목적 : 시뮬레이션은 가볍게 돌리는 목적이므로, 시뮬레이션 할 경우 저장 로직을 따로 덜어내 부담을 줄였다.
     * 구현
     * 1. parameter를 통해 저장할 지 여부를 확인한다.
     * 2. 시뮬레션을 돌린다.
     * 3. 만일 저장한다면, 시뮬레이션 결과를 토대로 매칭된 쌍을 DB에 저장한다.
     * <p>
     * 공통 매칭 결과
     * 시뮬레이션 매칭 결과 : 전체 사용자 수, 매칭 쌍의 개수, 매칭되지 않는 사용자 수,
     * 매칭 시작 시간 : 남성 사용자 id와 여성 사용자 id에 대한 인덱스가 생성된 직후
     * <p>
     * 저장할 경우(isSave = true)
     *
     * @return 매칭 결과
     */
    public MatchResDto getResultOfMatching(boolean isSave) {
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
        scoreBoard = calculateAnimalTypeScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = calculateInterestScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = calculateMovieGenreScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);

        // index(key) -> ID(value)로 바꾸기
        Map<Integer, Long> indexToMaleUserId = maleUserIdIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<Integer, Long> indexToFemaleUserId = femaleUserIdIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));

        // 매칭 (greedy algorithm)
        simulatedMatchResult = simulateMatching(scoreBoard, indexToMaleUserId, indexToFemaleUserId);

        // 만일 매칭 결과 저장할 경우
        if (isSave) {
        }

        return MatchResDto.builder().totalUserCount(simulatedMatchResult.totalUserCount()).matchedPairCount(simulatedMatchResult.matchedPairCount()).unmatchedUserCount(simulatedMatchResult.unmatchedUserCount()).simulatedAt(simulatedAt).build();
    }

    /**
     * checkValidate
     *
     * <p>
     * 사용할 엔티티의 정합성을 검사
     * 1. 도메인 범위 검증(list가 비어있는지 여부 확인)
     * 2. 속성 값 누락 여부
     * 3. 중복 데이터 검사
     *
     * @param list 검사할 엔티티들
     * @param type 검사할 엔티티 타입
     * @throws GeneralException 데이터 정합성 검증 실패 발생
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

    /**
     * calculateAnimalTypeScore
     *
     * <p>
     * 점수처리
     * 점수는 60점 혹은 0점인 이분적으로 처리한다.
     * 하나라도 일치할 경우, 만점을 주도록 처리해야 한다.
     * "상관 없음"에 대한 처리 : {@link UserMatchRepository#findUserAnimalMatchCandidates(Gender, Status)}
     * <p>
     * 매칭 필터 : 동물상 반드시 하나 이상 선택해야 한다.
     * 1개라도 매칭되지 않을 경우, score[][]의 값은 0값을 가지지만, 이 부분은 자동적으로 처리되므로 구현 부분은 따로 없다.
     * <p>
     * 일관성 처리
     * 경우 1
     * 상황 : 만일 인덱스 생성 전 (maleUserIdIndex, femaleUserIdIndex) 데이터가 추가 될 경우
     * 대처 : 만일 인덱스에 없는 데이터가 존재하면, null로 반환할테니 continue로 건너띈다.
     * 결국 index 생성 시점을 기준으로 일관성을 유지한다.
     * 경우 2
     * 상황 : 만일 기존 데이터가 수정되었을 경우
     * 대처 : 속성값이 null인지 확인하며 사용한다.
     * index 생성 시점 이후에 발생할 수 있는 문제에 대처할 수 있다.
     * <p>
     * 역순에 대한 처리 : 만일 순서가 뒤바뀌어도 로직이 제대로 작동하도록 구현하였다.
     * 상황 : 원래 동물상에 대한 점수를 먼저 매긴다는 전제로 구현했지만,
     * 통계를 위한 별도 계산이나 기획의 변경으로 인한 돌발 상황에 대처하기 위해 필요성을 느꼈다.
     * isVisit을 통해 방문 여부를 따로 체크하였다.
     * "+="연산으로 점수가 중첩되도록 하였다.
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 동물상 점수가 반영된 사용자간 점수 인접 리스트
     */
    private int[][] calculateAnimalTypeScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        List<UserAnimalMatchCandidate> maleUser = userMatchRepository.findUserAnimalMatchCandidates(Gender.MALE, Status.SUBMITTED);      // 제출한 남성 사용자의 id와 동물상, 선호하는 동물상 데이터를 가져온다.
        List<UserAnimalMatchCandidate> femaleUser = userMatchRepository.findUserAnimalMatchCandidates(Gender.FEMALE, Status.SUBMITTED);    // 제출한 여성 사용자의 id와 동물상, 선호하는 동물상 데이터를 가져온다.
        boolean[][] isVisit = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];   // scoreBoard 방문 여부 체크

        if (maleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.MALE_USER_ANIMAL_MATCH_CANDIDATE);    // 동물상 계산을 위한 남성 사용자 검증
        if (femaleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.FEMALE_USER_ANIMAL_MATCH_CANDIDATE); // 동물상 계산을 위한 여성 사용자 검증

        for (UserAnimalMatchCandidate m : maleUser) {
            if (m == null) continue; // 일관성(경우 1) 지켜주기 위한 코드
            for (UserAnimalMatchCandidate f : femaleUser) {
                if (f == null) continue; // 일관성(경우 1) 지켜주기 위한 코드
                if (m.animalTypeId().equals(f.preferredAnimalTypeId()) ||       // 남성 사용자의 동물상 = 여성 사용자의 선호하는 동물상 OR
                        m.preferredAnimalTypeId().equals(f.animalTypeId())) {   // 여성 사용자의 동물상 = 남성 사용자의 선호하는 동물상
                    Integer mIndex = maleUserIdIndex.get(m.userId());    // 남성 사용자 ID의 index값
                    Integer fIndex = femaleUserIdIndex.get(f.userId());  // 여성 사용자 ID의 index값

                    if (mIndex == null || fIndex == null) continue;   // 일관성(경우 2) 지켜주기 위한 코드

                    if (isVisit[mIndex][fIndex]) { // 이미 점수를 부여했을 경우 : skip
                        continue;
                    }

                    scoreBoard[mIndex][fIndex] += 60; // 점수 부여하지 않을 경우 : 60점을 더한다.
                    isVisit[mIndex][fIndex] = true;
                }
            }
        }

        return scoreBoard;
    }

    /**
     * calculateInterestScore
     * <p>
     * 개별로 점수를 계산한다.(Interest : 개당 10점 총 30점)
     * <p>
     * 주의! : 해당 로직에선 사용자가 관심사를 얼마나 선택했는지 검증하는 과정을 포함하지 않는다.
     * 전제 : 한 사용자에 대해, DB에 저장된 USER_INTERST는 3개씩 저장되어 있다고 가정한다.
     * 문제 상황
     * 따로 검증 구현 하기 위해 DB내부에서 다음과 같은 로직을 사용한다.
     * 1. 사용자를 성별로 두 그룹을 나눈다.
     * 2. 각 그룹과 USER_INTEREST간 JOIN연산 수행한다.
     * 3. 두 그룹간 interest_id를 기준으로 JOIN연산을 수행한다.
     * 이후 가져온 결과에 대해 개수를 체크해서 검증하는 과정을 수행하고자 했다.
     * 그러나 JPQL에선 서브 쿼리 수행이 불가능하기에(나눈 그룹 각각 관심사와 JOIN연산 불가능)하기에 해당 부분은 보류하기로 했다.
     * <p>
     * 매칭 필터 : 관심사 반드시 하나 이상 선택해야 한다.
     * 1개라도 매칭되지 않을 경우, score[][]의 값은 0값을 가진다.
     * <p>
     * 일관성 처리
     * 경우 1
     * 상황 : 만일 인덱스 생성 전 (maleUserIdIndex, femaleUserIdIndex) 데이터가 추가 될 경우
     * 대처 : 만일 인덱스에 없는 데이터가 존재하면, null로 반환할테니 continue로 건너띈다.
     * 결국 index 생성 시점을 기준으로 일관성을 유지한다.
     * 경우 2
     * 상황 : 만일 기존 데이터가 수정되었을 경우
     * 대처 : 속성값이 null인지 확인하며 사용한다.
     * index 생성 시점 이후에 발생할 수 있는 문제에 대처할 수 있다.
     * <p>
     * 역순에 대한 처리 : 만일 순서가 뒤바뀌어도 로직이 제대로 작동하도록 구현하였다.
     * "+="연산으로 점수가 중첩되도록 하였다.
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 관심사 점수가 반영된 사용자간 점수 인접 리스트
     */
    private int[][] calculateInterestScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        List<UserInterestMatchCandidate> maleUserList = userMatchRepository.findUserInterestMatchCandidates(Gender.MALE, Status.SUBMITTED);    // 제출한 남성 사용자의 id와 관심사 데이터를 가져온다.
        List<UserInterestMatchCandidate> femaleUserList = userMatchRepository.findUserInterestMatchCandidates(Gender.FEMALE, Status.SUBMITTED);  // 제출한 여성 사용자의 id와 관심사 데이터를 가져온다.

        if (maleUserList.isEmpty())
            throw new GeneralException(MatchErrorCode.MALE_USER_INTEREST_MATCH_CANDIDATE);     // 관심사 매칭 점수 계산 전 남성 사용자 후보 리스트를 검증한다.
        if (femaleUserList.isEmpty())
            throw new GeneralException(MatchErrorCode.FEMALE_USER_INTEREST_MATCH_CANDIDATE);   // 관심사 매칭 점수 계산 전 여성 사용자 후보 리스트를 검증한다.

        Set<UserInterestMatchCandidate> maleUserSet = new HashSet<>(maleUserList);      // 중복 제거를 위해, 남성 사용자: List -> Set으로 변환한다.
        Set<UserInterestMatchCandidate> femaleUserSet = new HashSet<>(femaleUserList);  // 중복 제거를 위해, 여성 사용자: List -> Set으로 변환한다.

        boolean[][] isVisit = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];    // scoreBoard 방문여부 체크

        for (UserInterestMatchCandidate m : maleUserSet) {
            if (m == null) continue;        // 일관성(경우 1) 지켜주기 위한 코드
            for (UserInterestMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // 일관성(경우 1) 지켜주기 위한 코드

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // 일관성(경우 2) 지켜주기 위한 코드

                if (m.interestId().equals(f.interestId())) {    // 관심사가 서로 같은가?
                    if (scoreBoard[mIndex][fIndex] > 0) {        // 매칭 필터(동물상 1개 이상 선택)를 통과 했는가?
                        scoreBoard[mIndex][fIndex] += 10;       // 관심사 매칭 점수 부여(중첩)
                        isVisit[mIndex][fIndex] = true;         // 방문 체크
                    }
                }
            }
        }

        for (int m = 0; m < isVisit.length; m++) {
            for (int f = 0; f < isVisit[m].length; f++) {
                if (!isVisit[m][f]) {         // 관심사 하나라도 선택 안했는가?
                    scoreBoard[m][f] = 0;   // 점수 0점 처리
                }
            }
        }

        return scoreBoard;
    }

    /**
     * calculateMovieGenreScore
     * <p>
     * 개별로 점수를 계산된다.(MovieGenre : 개당 5점 총 10점)
     * <p>
     * 주의! : 해당 로직에선 사용자가 관심사를 얼마나 선택했는지 검증하는 과정을 포함하지 않는다.
     * 전제 : 한 사용자에 대해, DB에 저장된 USER_INTERST는 3개씩 저장되어 있다고 가정한다.
     * {@link #calculateInterestScore}와 같은 이유이다.
     * <p>
     * 매칭 필터 : 영화 장르 반드시 하나 이상 선택해야 한다.
     * 1개라도 매칭되지 않을 경우, score[][]의 값은 0값을 가진다.
     * <p>
     * 일관성 처리
     * 경우 1
     * 상황 : 만일 인덱스 생성 전 (maleUserIdIndex, femaleUserIdIndex) 데이터가 추가 될 경우
     * 대처 : 만일 인덱스에 없는 데이터가 존재하면, null로 반환할테니 continue로 건너띈다.
     * 결국 index 생성 시점을 기준으로 일관성을 유지한다.
     * 경우 2
     * 상황 : 만일 기존 데이터가 수정되었을 경우
     * 대처 : 속성값이 null인지 확인하며 사용한다.
     * index 생성 시점 이후에 발생할 수 있는 문제에 대처할 수 있다.
     * <p>
     * 역순에 대한 처리 : 만일 순서가 뒤바뀌어도 로직이 제대로 작동하도록 구현하였다.
     * "+="연산으로 점수가 중첩되도록 하였다.
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 영화 장르 점수가 반영된 사용자간 점수 인접 리스트
     *
     */
    private int[][] calculateMovieGenreScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        List<UserMovieGenreMatchCandidate> maleUser = userMatchRepository.findUserMovieGenreMatchCandidate(Gender.MALE, Status.SUBMITTED);   // 제출한 남성 사용자의 id와 영화 장르 데이터를 가져온다.
        List<UserMovieGenreMatchCandidate> femaleUser = userMatchRepository.findUserMovieGenreMatchCandidate(Gender.FEMALE, Status.SUBMITTED); // 제출한 여성 사용자의 id와 영화 장르 데이터를 가져온다.

        if (maleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.MALE_USER_MOVIE_GENRE_MATCH_CANDIDATE);      // 영화 장르 매칭 점수 계산 전 남성 사용자 후보 리스트를 검증한다.
        if (femaleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.FEMALE_USER_MOVIE_GENRE_MATCH_CANDIDATE);    // 영화 장르 매칭 점수 계산 전 남성 사용자 후보 리스트를 검증한다.

        Set<UserMovieGenreMatchCandidate> maleUserSet = new HashSet<>(maleUser);        // 중복 제거를 위해, 남성 사용자: List -> Set으로 변환한다.
        Set<UserMovieGenreMatchCandidate> femaleUserSet = new HashSet<>(femaleUser);    // 중복 제거를 위해, 여성 사용자: List -> Set으로 변환한다.

        boolean[][] isVisit = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];    // scoreBoard 방문여부 체크

        for (UserMovieGenreMatchCandidate m : maleUserSet) {
            if (m == null) continue;        // 일관성(경우 1) 지켜주기 위한 코드
            for (UserMovieGenreMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // 일관성(경우 1) 지켜주기 위한 코드

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // 일관성(경우 2) 지켜주기 위한 코드

                if (m.movieGenreId().equals(f.movieGenreId())) {    // 영화 장르가 서로 같은가?
                    if (scoreBoard[mIndex][fIndex] > 0) {            // 매칭 필터(동물상, 관심사 1개 이상 선택)를 통과 했는가?
                        scoreBoard[mIndex][fIndex] += 10;           // 관심사 매칭 점수 부여(중첩)
                        isVisit[mIndex][fIndex] = true;             // 방문 체크
                    }
                }
            }
        }

        for (int m = 0; m < isVisit.length; m++) {
            for (int f = 0; f < isVisit[m].length; f++) {
                if (!isVisit[m][f]) {         // 영화 장르 하나라도 선택 안했는가?
                    scoreBoard[m][f] = 0;   // 점수 0점 처리
                }
            }
        }

        return scoreBoard;
    }

    /**
     * simulateMatching
     *
     * <p>
     * Greedy algorithm을 기본 베이스로 하는 로직이다.
     * filter 조건 : 점수가 높은 순으로 적용
     * <p>
     * 동점 처리 기준에 따른 처리
     * 1. 관심사가 더 많이 겹치는 사람
     * 구현 : score에 자동적으로 처리된다.(동물상, 관심사, 영화 장르 점수 처리를 동시에 하기 때문이다.)
     * 2. 제출 시간이 더 빠른 사람
     * 구현 : DB에 가져온 순서 그대로 적용된다.
     * 3. 랜덤 매칭
     * 구현 : DB 시스템 내부서 정해진 무작위성을 따른다.
     * <p>
     * 필터여부 : 동물상, 관심사, 영화 장르 각 기준에서 하나 이상 겹쳐지지 않으면 거른다.
     * 구현 : 0점 이하는 거른다.
     *
     * @param scoreBoard          사용자간 점수 인접 리스트
     * @param indexToMaleUserId   남성 사용자 id로 변환해주는 index
     * @param indexToFemaleUserId 여성 사용자 id로 변환해주는 index
     * @return 점수를 토대로 도출한 매칭 결과 리스트
     */
    private TempMatchResult simulateMatching(int[][] scoreBoard, Map<Integer, Long> indexToMaleUserId, Map<Integer, Long> indexToFemaleUserId) {
        Integer totalUserCount = indexToFemaleUserId.size() + indexToMaleUserId.size();
        Integer matchedPairCount = 0;
        Integer unmatchedUserCount = 0;

        // 모든 매칭을 나열
        List<TempMatch> relations = new ArrayList<>();

        for (int m = 0; m < scoreBoard.length; m++) {
            for (int f = 0; f < scoreBoard[m].length; f++) {
                // 필터 적용
                if (scoreBoard[m][f] == 0) {
                    continue;
                }
                relations.add(new TempMatch(m, f, scoreBoard[m][f]));
            }
        }

        // 매칭 점수가 높은 순으로, 내림차순 정렬
        relations.sort(Comparator.comparingInt(TempMatch::score).reversed());

        // 방문 여부를 체크하기 위한 배열
        boolean[] isVisitMaleUser = new boolean[indexToMaleUserId.size()];
        boolean[] isVisitFemaleUser = new boolean[indexToFemaleUserId.size()];

        // 그리디 알고리즘
        for (TempMatch tm : relations) {
            if (!isVisitMaleUser[tm.maleUserIndex()] && !isVisitFemaleUser[tm.femaleUserIndex()]) {
                isVisitMaleUser[tm.maleUserIndex()] = true;
                isVisitFemaleUser[tm.femaleUserIndex()] = true;
                matchedPairCount++;
            }
        }

        // 매칭되지 않는 사용자 수
        unmatchedUserCount += (int) IntStream.range(0, isVisitMaleUser.length).filter(i -> !isVisitMaleUser[i]).count();
        unmatchedUserCount += (int) IntStream.range(0, isVisitFemaleUser.length).filter(i -> !isVisitFemaleUser[i]).count();

        return new TempMatchResult(totalUserCount, matchedPairCount, unmatchedUserCount);
    }
}
