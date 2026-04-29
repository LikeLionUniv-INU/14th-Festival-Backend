package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.dto.MatchRequest;
import com.likelion.zooting.domain.match.policy.MatchPolicy;
import com.likelion.zooting.domain.match.policy.MatchScoreCalculatePolicy;
import com.likelion.zooting.domain.match.policy.MatchValidationType;
import com.likelion.zooting.domain.match.service.converter.MatchCandidatesConverter;
import com.likelion.zooting.domain.match.service.converter.MatchDataConverter;
import com.likelion.zooting.domain.match.service.converter.MatchUserConverter;
import com.likelion.zooting.domain.match.service.data.TempMatchResult;
import com.likelion.zooting.domain.match.service.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.usermoviegenre.repository.UserMovieGenreRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import com.likelion.zooting.domain.userpreferredanimaltype.repository.UserPreferredAnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
public class MatchService extends MatchSaveService {
    private final MatchPolicy matchPolicy;
    private final MatchScoreCalculatePolicy matchScoreCalculatePolicy;
    private final UserRepository userRepository;
    private final UserPreferredAnimalTypeRepository userPreferredAnimalTypeRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserMovieGenreRepository userMovieGenreRepository;
    private final MatchUserConverter matchUserConverter;
    private final MatchDataConverter matchDataConverter;
    private final MatchCandidatesConverter matchCandidatesConverter;
    private final MatchValidateService matchValidateService;

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
        matchValidateService.checkValidate(maleUsers, MatchValidationType.MALE_USER_ID);
        matchValidateService.checkValidate(femaleUsers, MatchValidationType.FEMALE_USER_ID);

        // repository에서 사용자 관련 데이터(선호 동물상, 관심사, 영화 장르) 가져오기
        List<UserPreferredAnimalType> userPreferredAnimalTypes = userPreferredAnimalTypeRepository.findAll();
        List<UserInterest> userInterests = userInterestRepository.findAll();
        List<UserMovieGenre> userMovieGenres = userMovieGenreRepository.findAll();
        // 검정
        matchValidateService.checkValidate(userPreferredAnimalTypes, MatchValidationType.PREFERRED_ANiMAL_ID);
        matchValidateService.checkValidate(userInterests, MatchValidationType.INTEREST_ID);
        matchValidateService.checkValidate(userMovieGenres, MatchValidationType.MOVIE_ID);

        // 사용자 ID -> index
        // 다른 데이터의 유입은 없으며, 사용자에 대한 검정은 거쳤으므로 해당 검정 과정은 넘어간다.
        Map<Long, Integer> maleUserIdToIndex = matchUserConverter.getUserIdToIndex(maleUsers);
        Map<Long, Integer> femaleUserIdToIndex = matchUserConverter.getUserIdToIndex(femaleUsers);

        // index -> 사용자 ID
        // 다른 데이터의 유입은 없으며, 사용자에 대한 검정은 거쳤으므로 해당 검정 과정은 넘어간다.
        Map<Integer, Long> indexToMaleUserId = matchUserConverter.getIndexToUserId(maleUsers);
        Map<Integer, Long> indexToFemaleUserId = matchUserConverter.getIndexToUserId(femaleUsers);

        // 동물상과 관심 동물상, 사용자를 매핑한 리스트
        List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates = matchCandidatesConverter.getUserAnimalMatchCandidates(maleUsers, userPreferredAnimalTypes);
        List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates = matchCandidatesConverter.getUserAnimalMatchCandidates(femaleUsers, userPreferredAnimalTypes);
        // 검정
        matchValidateService.checkValidate(maleUserAnimalMatchCandidates, MatchValidationType.MALE_PREFERRED_ANiMAL_ID);
        matchValidateService.checkValidate(femaleUserAnimalMatchCandidates, MatchValidationType.FEMALE_PREFERRED_ANiMAL_ID);

        // 관심사와 사용자 매핑한 리스트
        List<UserInterestMatchCandidate> maleUserInterestMatchCandidates = matchCandidatesConverter.getUserInterestMatchCandidates(maleUsers, userInterests);
        List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates = matchCandidatesConverter.getUserInterestMatchCandidates(femaleUsers, userInterests);
        // 검정
        matchValidateService.checkValidate(maleUserInterestMatchCandidates, MatchValidationType.MALE_INTEREST_ID);
        matchValidateService.checkValidate(femaleUserInterestMatchCandidates, MatchValidationType.FEMALE_INTEREST_ID);

        // 영화 장르와 사용자 매핑한 리스트
        List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates = matchCandidatesConverter.getUserMovieGenreMatchCandidates(maleUsers, userMovieGenres);
        List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates = matchCandidatesConverter.getUserMovieGenreMatchCandidates(femaleUsers, userMovieGenres);
        // 검정
        matchValidateService.checkValidate(maleUserMovieGenreMatchCandidates, MatchValidationType.MALE_MOVIE_ID);
        matchValidateService.checkValidate(femaleUserMovieGenreMatchCandidates, MatchValidationType.FEMALE_MOVIE_ID);

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

}
