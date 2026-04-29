package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.service.data.TempMatch;
import com.likelion.zooting.domain.match.service.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.service.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchSaveService{
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
