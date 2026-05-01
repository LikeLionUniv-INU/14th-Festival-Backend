package com.likelion.zooting.domain.match.service.converter;

import com.likelion.zooting.domain.match.service.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserMovieGenreMatchCandidate;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MatchCandidateConvertorByMap {
    /**
     * 사용자별 동물상 데이터에 대한 조회 효율을 위해 Map 구조로 변환한다.
     *
     * @param userAnimalMatchCandidates 사용자별 동물상 리스트
     * @return (사용자 ID -> 동물상 ID) Map
     */
    public Map<Long, Long> animalCandidateToMap(List<UserAnimalMatchCandidate> userAnimalMatchCandidates) {
        return userAnimalMatchCandidates.stream()
                .collect(Collectors.toMap(
                        uamc -> uamc.userId(),
                        uamc -> uamc.animalTypeId(),
                        (existing, replacement) -> existing));
    }

    /**
     * 사용자별 선호 동물상 데이터에 대한 조회 효율을 위해 Map 구조로 변환한다.
     *
     * @param userAnimalMatchCandidates 사용자별 동물상 리스트
     * @return (사용자 ID -> 선호 동물상 ID List) Map
     */
    public Map<Long, List<Long>> preferredAnimalCandidateToMap(List<UserAnimalMatchCandidate> userAnimalMatchCandidates) {
        return userAnimalMatchCandidates.stream()
                .collect(Collectors.groupingBy(
                        UserAnimalMatchCandidate::userId, // key : 사용자 ID
                        Collectors.mapping(UserAnimalMatchCandidate::preferredAnimalTypeId, Collectors.toList()) // value : 선호 동물상 ID 리스트
                ));
    }

    /**
     * 사용자별 관심사 데이터에 대한 조회 효율을 위해 Map 구조로 변환한다.
     *
     * @param userInterestMatchCandidates 사용자별 관심사 리스트
     * @return (사용자 ID -> 관심사 ID List) Map
     */
    public Map<Long, List<Long>> interestCandidateToMap(List<UserInterestMatchCandidate> userInterestMatchCandidates) {
        return userInterestMatchCandidates.stream()
                .collect(Collectors.groupingBy(
                        UserInterestMatchCandidate::userId, // key : 사용자 ID
                        Collectors.mapping(UserInterestMatchCandidate::interestId, Collectors.toList()) // value : 관심사 ID
                ));
    }

    /**
     * 사용자별 영화 장르 데이터에 대한 조회 효율을 위해 Map 구조로 변환한다.
     *
     * @param userMovieGenreMatchCandidates 사용자별 영화 장르 리스트
     * @return (사용자 ID -> 영화 장르 ID List) Map
     */
    public Map<Long, List<Long>> movieGenreCandidateToMap(List<UserMovieGenreMatchCandidate> userMovieGenreMatchCandidates) {
        return userMovieGenreMatchCandidates.stream()
                .collect(Collectors.groupingBy(
                        UserMovieGenreMatchCandidate::userId,
                        Collectors.mapping(UserMovieGenreMatchCandidate::movieGenreId, Collectors.toList())
                ));
    }
}
