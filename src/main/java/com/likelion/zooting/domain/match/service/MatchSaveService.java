package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.policy.MatchCountPolicy;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.match.service.converter.MatchCandidateConvertorByMap;
import com.likelion.zooting.domain.match.service.data.*;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MatchSaveService {    // 이부분 수정(extends)
    private final MatchRepository matchRepository;
    private final MatchCountPolicy matchCountPolicy;
    private final MatchCandidateConvertorByMap matchCandidateConvertorByMap;
    private Logger log;

    /**
     * 시뮬레이션 결과를 토대로 매칭된 쌍들을 DB에 저장합니다.
     * <p><b>[반환 값의 설명]</b></p>
     * <ul>
     * <li><b>false : </b>이미 한 번 실행한 경우(Match 저장 여부로 판단)</li>
     * <li><b>true : </b>시뮬레이션 결과물을 DB에 저장</li>
     * </ul>
     * <p><b>[중복 판단 방식]</b></p>
     * <ul>
     * <li>현 방식은 단순히 모든 Match를 가져와 하나라도 존재하는지 판단한다.</li>
     * <li><s>모든 Match를 가져오는 방식은 위험하지만, 우리 서비스를 이용할 약 200 명의 규모에선 괜찮다고 판단했다.</s> -> 남성, 여성 각각 확인하는 방식으로 찾는다.</li>
     * <li>현 방식을 방지하기 위해선, Match의 생성일자에 대한 속성을 추가하고, Option을 사용해 오늘 생성한 Match 중 하나만 가져오도록 하면 해결할 수 있다.</li>
     * <li>생성일자가 아니더라도, 우리가 알 수 있는 값이면 무엇이든 가능하다.</li>
     * </ul>
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
     */
    @Transactional  // -> public써야 한다.
    public void saveResultOfMatch(List<TempMatch> finalMatchedPairList,
                                  List<User> maleUsers,
                                  List<User> femaleUsers,
                                  List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates,
                                  List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates,
                                  List<UserInterestMatchCandidate> maleUserInterestMatchCandidates,
                                  List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates,
                                  List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates,
                                  List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates) {
        // 이미 한 번 실행한건가?-> DB에 남성 사용자에 대해 저장되어 있는 여부로 확인
        if (matchRepository.existsByMaleUser_GenderAndMaleUser_Status(Gender.MALE, Status.SUBMITTED)) {
            throw new GeneralException(MatchErrorCode.DUPLICATE_EXECUTION_OF_MATCH_SAVE);
        }


        Map<Long, Long> maleUserAnimalMap = matchCandidateConvertorByMap.animalCandidateToMap(maleUserAnimalMatchCandidates);
        Map<Long, Long> femaleUserAnimalMap = matchCandidateConvertorByMap.animalCandidateToMap(femaleUserAnimalMatchCandidates);
        Map<Long, List<Long>> maleUserPreferredAnimalMap = matchCandidateConvertorByMap.preferredAnimalCandidateToMap(maleUserAnimalMatchCandidates);
        Map<Long, List<Long>> femaleUserPreferredAnimalMap = matchCandidateConvertorByMap.preferredAnimalCandidateToMap(femaleUserAnimalMatchCandidates);
        Map<Long, List<Long>> maleUserInterestMap = matchCandidateConvertorByMap.interestCandidateToMap(maleUserInterestMatchCandidates);
        Map<Long, List<Long>> femaleUserInterestMap = matchCandidateConvertorByMap.interestCandidateToMap(femaleUserInterestMatchCandidates);
        Map<Long, List<Long>> maleUserMovieGenreMap = matchCandidateConvertorByMap.movieGenreCandidateToMap(maleUserMovieGenreMatchCandidates);
        Map<Long, List<Long>> femaleUserMovieGenreMap = matchCandidateConvertorByMap.movieGenreCandidateToMap(femaleUserMovieGenreMatchCandidates);

        List<Matches> pairs = finalMatchedPairList.stream()
                .map(pair -> new MatchedPair(
                        maleUsers.get(pair.maleUserIndex()),
                        femaleUsers.get(pair.femaleUserIndex()),
                        pair.score()
                ))
                .map(pair -> Matches.create(
                        pair.maleUser(),
                        pair.femaleUser(),
                        matchCountPolicy.getAnimalTypeCount(pair,
                                maleUserAnimalMap,
                                femaleUserAnimalMap,
                                maleUserPreferredAnimalMap,
                                femaleUserPreferredAnimalMap),
                        matchCountPolicy.getInterestCount(pair,
                                maleUserInterestMap,
                                femaleUserInterestMap),
                        matchCountPolicy.getMovieGenreCount(pair,
                                maleUserMovieGenreMap,
                                femaleUserMovieGenreMap),
                        pair.score()
                ))
                .toList();

        matchRepository.saveAll(pairs);
    }

}
