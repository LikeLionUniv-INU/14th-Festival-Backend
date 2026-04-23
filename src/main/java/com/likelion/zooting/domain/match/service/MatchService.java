package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.entity.Match;
import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
     *
     * @return 매칭 결과 리스트
     */
    public List<Match> getResultOfMatch() {
        // 남성 사용자 id와 여성 사용자 id에 대한 인덱스
        List<Long> maleUserIdList = userMatchRepository.findByGenderAndStatus(Gender.MALE, Status.SUBMITTED);
        List<Long> femaleUserIdList = userMatchRepository.findByGenderAndStatus(Gender.FEMALE, Status.SUBMITTED);

        // DB에서 가져온 list 검정
        checkValidate(maleUserIdList, MatchScoreType.MALE_USER_ID);
        checkValidate(femaleUserIdList, MatchScoreType.FEMALE_USER_ID);

        Map<Long, Integer> maleUserIdIndex = IntStream.range(0, maleUserIdList.size()).boxed().collect(Collectors.toMap(i -> maleUserIdList.get(i), i -> i));
        Map<Long, Integer> femaleUserIdIndex = IntStream.range(0, femaleUserIdList.size()).boxed().collect(Collectors.toMap(i -> femaleUserIdList.get(i), i -> i));

        // user간 점수 인접 리스트 초기화
        int[][] scoreBoard = new int[maleUserIdList.size()][femaleUserIdList.size()];

        // 점수 계산
        scoreBoard = calculateAnimalTypeScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = calculateInterestScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);
        scoreBoard = calculateMovieGenreScore(scoreBoard, maleUserIdIndex, femaleUserIdIndex);

        // 매칭 (Gale-Shapley algorithm)
        List<Match> matchResults = matching(scoreBoard, maleUserIdIndex, femaleUserIdIndex);

        return matchResults;
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
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 관심사 점수가 반영된 사용자간 점수 인접 리스트
     */
    private int[][] calculateInterestScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        return scoreBoard;
    }

    /**
     * calculateMovieGenreScore
     * <p>
     * 개별로 점수를 계산된다.(MovieGenre : 개당 5점 총 10점)
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 영화 장르 점수가 반영된 사용자간 점수 인접 리스트
     *
     */
    private int[][] calculateMovieGenreScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        return scoreBoard;
    }

    /**
     * matching
     *
     * <p>
     * Greedy algorithm을 기본 베이스로 하는 로직이다.
     * filter 조건 : 점수가 높은 순으로 적용
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 점수를 토대로 도출한 매칭 결과 리스트
     */
    private List<Match> matching(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        List<Match> matchResults = new ArrayList<>();

        return matchResults;
    }
}
