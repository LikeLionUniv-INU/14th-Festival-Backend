package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.entity.Match;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

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
        List<Long> maleUserIdList = userRepository.findByGenderAndStatus(Gender.MALE, Status.SUBMITTED);
        List<Long> femaleUserIdList = userRepository.findByGenderAndStatus(Gender.FEMALE, Status.SUBMITTED);

        // DB에서 가져온 list 검정
        checkValidate(maleUserIdList, MatchScoreType.MALE_USER_ID);
        checkValidate(femaleUserIdList, MatchScoreType.FEMALE_USER_ID);

        Map<Long, Integer> maleUserIdIndex = IntStream.range(0, maleUserIdList.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> maleUserIdList.get(i),
                        i -> i
                ));
        Map<Long, Integer> femaleUserIdIndex = IntStream.range(0, femaleUserIdList.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> femaleUserIdList.get(i),
                        i -> i
                ));

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
     * 점수는 60점 혹은 0점인 이분적으로 처리한다.
     * "상관없음"일 경우 만점을 주도록 처리를 따로 처리해야 한다.
     * "상관없은" 아닌 경우, 하나라도 일치할 경우, 만점을 주도록 처리해야 한다.
     *
     * @param scoreBoard        사용자간 점수 인접 리스트
     * @param maleUserIdIndex   남성 사용자 ID 인덱스
     * @param femaleUserIdIndex 여성 사용자 ID 인덱스
     * @return 동물상 점수가 반영된 사용자간 점수 인접 리스트
     */
    private int[][] calculateAnimalTypeScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
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
