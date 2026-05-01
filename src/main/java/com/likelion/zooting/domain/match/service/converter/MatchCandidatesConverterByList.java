package com.likelion.zooting.domain.match.service.converter;

import com.likelion.zooting.domain.match.service.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor    // final 필드의 생성자 자동 생성
public class MatchCandidatesConverterByList {
    private final MatchDataConverter matchDataConverter;

    /**
     * 사용자를 기준으로 동물상과 선호 동물상을 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchDataConverter}의 mapToUserAnimalMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users                사용자 리스트
     * @param preferredAnimalTypes 선호 동물상 리스트
     * @return 사용자별 동물상과 선호 동물상을 매핑한 리스트
     */
    public List<UserAnimalMatchCandidate> getUserAnimalMatchCandidates(List<User> users, List<UserPreferredAnimalType> preferredAnimalTypes) {
        List<UserAnimalMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserPreferredAnimalType upa : preferredAnimalTypes) {
                if (upa.getUser().equals(u)) {
                    results.add(matchDataConverter.mapToUserAnimalMatchCandidate(u, upa));
                }
            }
        }

        return results;
    }

    /**
     * 사용자를 기준으로 관심사를 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchDataConverter}의 mapToUserInterestMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users         사용자 리스트
     * @param userInterests 관심사 리스트
     * @return 사용자별 관심사 매핑한 리스트
     */
    public List<UserInterestMatchCandidate> getUserInterestMatchCandidates(List<User> users, List<UserInterest> userInterests) {
        List<UserInterestMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserInterest ui : userInterests) {
                if (ui.getUser().equals(u)) {
                    results.add(matchDataConverter.mapToUserInterestMatchCandidate(u, ui));
                }
            }
        }

        return results;
    }

    /**
     * 사용자 기준으로 영화 장르를 매핑한 리스트를 만들어냅니다.
     * <p>
     * {@link MatchDataConverter}의 mapToUserMovieGenreMatchCandidate란 매퍼를 사용합니다.
     * </p>
     *
     * @param users           사용자 리스트
     * @param userMovieGenres 영화 장르 리스트
     * @return 사용자별 영화 장르 매핑한 리스트
     */
    public List<UserMovieGenreMatchCandidate> getUserMovieGenreMatchCandidates(List<User> users, List<UserMovieGenre> userMovieGenres) {
        List<UserMovieGenreMatchCandidate> results = new ArrayList<>();
        for (User u : users) {
            for (UserMovieGenre umg : userMovieGenres) {
                if (umg.getUser().equals(u)) {
                    results.add(matchDataConverter.mapToUserMovieGenreMatchCandidate(u, umg));
                }
            }
        }

        return results;
    }
}
