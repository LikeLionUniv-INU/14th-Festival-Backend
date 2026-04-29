package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.mapper.MatchMapper;
import com.likelion.zooting.domain.match.policy.MatchPolicy;
import com.likelion.zooting.domain.match.policy.MatchScoreCalculatePolicy;
import com.likelion.zooting.domain.match.policy.MatchScoreType;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.match.service.data.*;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.usermoviegenre.repository.UserMovieGenreRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.UserPreferredAnimalTypeRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import com.likelion.zooting.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
public class MatchService {
    private final MatchPolicy matchPolicy;
    private final MatchScoreCalculatePolicy matchScoreCalculatePolicy;

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final UserPreferredAnimalTypeRepository userPreferredAnimalTypeRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserMovieGenreRepository userMovieGenreRepository;

    private final MatchMapper matchMapper;

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
     * <p><b>[해당 로직의 전제조건]</b></p>
     * 본 로직은 대량의데이터를 효율적으로 처리하기 위해 사전 검증된 데이터만을 입력 값으로 받는것을 원칙으로 합니다.
     * <ul>
     * <li><b>전제 1. 데이터 넣을때, 개수에 맞게 넣음 :</b> 따로 사용자별 선호 동물상, 관심사, 영화 장르 선택 개수를 체크하지 않습니다.</li>
     * <li><b>전제 2. 과거의 데이터 사용하지 않음 :</b> 18:00시 이후 모든 데이터를 비운다는 가정하에, 정합성이 무너지지 않는다는 전제로 실행됩니다.</li>
     * </ul>
     *
     * @param isSave DB 저장 여부 (true: 저장 수행, false: 시뮬레이션 결과만 반환)
     * @return 매칭 결과 통계 및 정보가 담긴 DTO
     */
    public MatchRequest getResultOfMatching(boolean isSave) {
        // 매칭 결과
        TempMatchResult simulatedMatchResult;
        LocalDateTime simulatedAt;


        // repository에서 사용자 데이터 가져오기
        List<User> maleUsers = userRepository.findByGenderAndStatus(Gender.MALE, Status.SUBMITTED);
        List<User> femaleUsers = userRepository.findByGenderAndStatus(Gender.FEMALE, Status.SUBMITTED);
        // 검정
        checkValidate(maleUsers, MatchScoreType.MALE_USER_ID);
        checkValidate(femaleUsers, MatchScoreType.FEMALE_USER_ID);

        // repository에서 사용자 관련 데이터(선호 동물상, 관심사, 영화 장르) 가져오기
        List<UserPreferredAnimalType> userPreferredAnimalTypes = userPreferredAnimalTypeRepository.findAll();
        List<UserInterest> userInterests = userInterestRepository.findAll();
        List<UserMovieGenre> userMovieGenres = userMovieGenreRepository.findAll();
        // 검정
        checkValidate(userPreferredAnimalTypes, MatchScoreType.PREFERRED_ANiMAL_ID);
        checkValidate(userInterests, MatchScoreType.INTEREST_ID);
        checkValidate(userMovieGenres, MatchScoreType.MOVIE_ID);

        // 사용자 ID -> index
        // 다른 데이터의 유입은 없으며, 사용자에 대한 검정은 거쳤으므로 해당 검정 과정은 넘어간다.
        Map<Long, Integer> maleUserIdToIndex = getUserIdToIndex(maleUsers);
        Map<Long, Integer> femaleUserIdToIndex = getUserIdToIndex(femaleUsers);

        // index -> 사용자 ID
        // 다른 데이터의 유입은 없으며, 사용자에 대한 검정은 거쳤으므로 해당 검정 과정은 넘어간다.
        Map<Integer, Long> indexToMaleUserId = getIndexToUserId(maleUsers);
        Map<Integer, Long> indexToFemaleUserId = getIndexToUserId(femaleUsers);

        // 동물상과 관심 동물상, 사용자를 매핑한 리스트
        List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates = getUserAnimalMatchCandidates(maleUsers, userPreferredAnimalTypes);
        List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates = getUserAnimalMatchCandidates(femaleUsers, userPreferredAnimalTypes);
        // 검정
        checkValidate(maleUserAnimalMatchCandidates, MatchScoreType.MALE_PREFERRED_ANiMAL_ID);
        checkValidate(femaleUserAnimalMatchCandidates, MatchScoreType.FEMALE_PREFERRED_ANiMAL_ID);

        // 관심사와 사용자 매핑한 리스트
        List<UserInterestMatchCandidate> maleUserInterestMatchCandidates = getUserInterestMatchCandidates(maleUsers, userInterests);
        List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates = getUserInterestMatchCandidates(femaleUsers, userInterests);
        // 검정
        checkValidate(maleUserInterestMatchCandidates, MatchScoreType.MALE_INTEREST_ID);
        checkValidate(femaleUserInterestMatchCandidates, MatchScoreType.FEMALE_INTEREST_ID);

        // 영화 장르와 사용자 매핑한 리스트
        List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates = getUserMovieGenreMatchCandidates(maleUsers, userMovieGenres);
        List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates = getUserMovieGenreMatchCandidates(femaleUsers, userMovieGenres);
        // 검정
        checkValidate(maleUserMovieGenreMatchCandidates, MatchScoreType.MALE_MOVIE_ID);
        checkValidate(femaleUserMovieGenreMatchCandidates, MatchScoreType.FEMALE_MOVIE_ID);

        simulatedAt = LocalDateTime.now();  // 매칭 시작 시간

        // user간 점수 인접 리스트 초기화
        int[][] scoreBoard = new int[maleUsers.size()][femaleUsers.size()];

        // 점수 계산
        scoreBoard = matchScoreCalculatePolicy.calculateAnimalTypeScore(scoreBoard, maleUserIdToIndex, femaleUserIdToIndex, maleUserAnimalMatchCandidates, femaleUserAnimalMatchCandidates);
        scoreBoard = matchScoreCalculatePolicy.calculateInterestScore(scoreBoard, maleUserIdToIndex, femaleUserIdToIndex, maleUserInterestMatchCandidates, femaleUserInterestMatchCandidates);
        scoreBoard = matchScoreCalculatePolicy.calculateMovieGenreScore(scoreBoard, maleUserIdToIndex, femaleUserIdToIndex, maleUserMovieGenreMatchCandidates, femaleUserMovieGenreMatchCandidates);

        // 매칭 (greedy algorithm)
        simulatedMatchResult = matchPolicy.simulateMatching(scoreBoard, indexToMaleUserId, indexToFemaleUserId);

        // 만일 매칭 결과 저장할 경우
        if (isSave) {
            if (saveResultOfMatch(simulatedMatchResult.finalMatchedPairList(),
                    maleUsers,
                    femaleUsers,
                    maleUserAnimalMatchCandidates,
                    femaleUserAnimalMatchCandidates,
                    maleUserInterestMatchCandidates,
                    femaleUserInterestMatchCandidates,
                    maleUserMovieGenreMatchCandidates,
                    femaleUserMovieGenreMatchCandidates)) {

            }
        }

        return MatchRequest.builder().totalUserCount(simulatedMatchResult.totalUserCount()).matchedPairCount(simulatedMatchResult.matchedPairCount()).unmatchedUserCount(simulatedMatchResult.unmatchedUserCount()).simulatedAt(simulatedAt).build();
    }

    /**
     * 사용자 리스트를 "사용자 ID -> 인덱스" 맵으로 변환합니다.
     *
     * @param users 사용자 리스트
     * @return 사용자 ID -> 인덱스 맵
     */
    private Map<Long, Integer> getUserIdToIndex(List<User> users) {
        return IntStream.range(0, users.size()).boxed().collect(Collectors.toMap(i -> users.get(i).getUserId(), i -> i));
    }

    /**
     * 사용자 리스트를 "인덱스 -> 사용자 ID" 맵으로 변환합니다.
     *
     * @param users 사용자 리스트
     * @return 인덱스 -> 사용자 ID 맵
     */
    private Map<Integer, Long> getIndexToUserId(List<User> users) {
        return IntStream.range(0, users.size()).boxed().collect(Collectors.toMap(i -> i, i -> users.get(i).getUserId()));
    }

    /**
     * 사용자를 기준으로 동물상과 선호 동물상을 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchMapper}의 mapToUserAnimalMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users                사용자 리스트
     * @param preferredAnimalTypes 선호 동물상 리스트
     * @return 사용자별 동물상과 선호 동물상을 매핑한 리스트
     */
    private List<UserAnimalMatchCandidate> getUserAnimalMatchCandidates(List<User> users, List<UserPreferredAnimalType> preferredAnimalTypes) {
        List<UserAnimalMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserPreferredAnimalType upa : preferredAnimalTypes) {
                if (upa.getUser().equals(u)) {
                    results.add(matchMapper.mapToUserAnimalMatchCandidate(u, upa));
                }
            }
        }

        return results;
    }

    /**
     * 사용자를 기준으로 관심사를 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchMapper}의 mapToUserInterestMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users         사용자 리스트
     * @param userInterests 관심사 리스트
     * @return 사용자별 관심사 매핑한 리스트
     */
    private List<UserInterestMatchCandidate> getUserInterestMatchCandidates(List<User> users, List<UserInterest> userInterests) {
        List<UserInterestMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserInterest ui : userInterests) {
                if (ui.getUser().equals(u)) {
                    results.add(matchMapper.mapToUserInterestMatchCandidate(u, ui));
                }
            }
        }

        return results;
    }

    /**
     * 사용자 기준으로 영화 장르를 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchMapper}의 mapToUserMovieGenreMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users           사용자 리스트
     * @param userMovieGenres 영화 장르 리스트
     * @return 사용자별 영화 장르 매핑한 리스트
     */
    private List<UserMovieGenreMatchCandidate> getUserMovieGenreMatchCandidates(List<User> users, List<UserMovieGenre> userMovieGenres) {
        List<UserMovieGenreMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserMovieGenre umg : userMovieGenres) {
                if (umg.getUser().equals(u)) {
                    results.add(matchMapper.mapToUserMovieGenreMatchCandidate(u, umg));
                }
            }
        }

        return results;
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
            throw new GeneralException(MatchScoreType.DUPLICATED_ID.getErrorCode());
        }
    }

    /**
     * 시뮬레이션 결과를 토대로 매칭된 쌍들을 DB에 저장합니다.
     *
     * @param finalMatchedPairList                시뮬레이션 결과물(누가 몇 점으로 매칭되었는지만 담고 있다.)
     * @param maleUsers                           남성 사용자
     * @param femaleUsers                         여성 사용자
     * @param maleUserAnimalMatchCandidates       남성 사용자와 동물상 매칭 후보들
     * @param femaleUserAnimalMatchCandidates     여성 사용자와 동물상 매칭 후보들
     * @param maleUserInterestMatchCandidates     남성 사용자와 관심사 매칭 후보들
     * @param femaleUserInterestMatchCandidates   여성 사용자와 관심사 매칭 후보들
     * @param maleUserMovieGenreMatchCandidates   남성 사용자와 영화 장르 매칭 후보들
     * @param femaleUserMovieGenreMatchCandidates 여성 사용자와 영화 장르 매칭 후보들
     * @return DB에 저장되었는지 여부(true / false)
     */
    @Transactional  // -> public써야 한다.
    public boolean saveResultOfMatch(List<TempMatch> finalMatchedPairList,
                                     List<User> maleUsers,
                                     List<User> femaleUsers,
                                     List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates,
                                     List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates,
                                     List<UserInterestMatchCandidate> maleUserInterestMatchCandidates,
                                     List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates,
                                     List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates,
                                     List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates) {
        return true;
    }

}
