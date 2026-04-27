package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.dto.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.match.exception.MatchInnerErrorCode;
import com.likelion.zooting.domain.match.policy.MatchScoreCalculatePolicy;
import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MatchScoreCalculatePolicyImpl implements MatchScoreCalculatePolicy {
    @Override
    public int[][] calculateAnimalTypeScore(int[][] scoreBoard,
                                            Map<Long, Integer> maleUserIdIndex,
                                            Map<Long, Integer> femaleUserIdIndex,
                                            List<UserAnimalMatchCandidate> maleUserAnimalMatchCandidates,
                                            List<UserAnimalMatchCandidate> femaleUserAnimalMatchCandidates) {
        boolean[][] maleToFemale = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];   // 동물상(남성) -> 선호 동물상(여성)
        boolean[][] femaleToMale = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];   // 동물상(여성) -> 선호 동물상(남성)

        for (UserAnimalMatchCandidate m : maleUserAnimalMatchCandidates) {
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserAnimalMatchCandidate f : femaleUserAnimalMatchCandidates) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치
                if (m.animalTypeId().equals(f.preferredAnimalTypeId()) ||       // 남성 사용자의 동물상 = 여성 사용자의 선호하는 동물상 OR
                        m.preferredAnimalTypeId().equals(f.animalTypeId())) {   // 여성 사용자의 동물상 = 남성 사용자의 선호하는 동물상
                    Integer mIndex = maleUserIdIndex.get(m.userId());    // 남성 사용자 ID의 index값
                    Integer fIndex = femaleUserIdIndex.get(f.userId());  // 여성 사용자 ID의 index값

                    if (mIndex == null || fIndex == null) continue;   // Case 2(데이터 수정)를 위한 장치

                    if (m.animalTypeId().equals(f.preferredAnimalTypeId())) {  // 동물상(남성) == 선호 동물상(여성)
                        maleToFemale[mIndex][fIndex] = true;
                    }
                    if (m.preferredAnimalTypeId().equals(f.animalTypeId())) {  // 동물상(남성) == 선호 동물상(여성)
                        femaleToMale[mIndex][fIndex] = true;
                    }
                }
            }
        }

        // Case 3(점수의 원소성) 보장을 위한 장치
        for (int m = 0; m < maleUserIdIndex.size(); m++) {
            for (int f = 0; f < femaleUserIdIndex.size(); f++) {
                if (maleToFemale[m][f] && femaleToMale[m][f]) {  // 서로 상대의 선호하는 동물상과 자신의 동물상이 일치하는가? (양방향성 확인)
                    scoreBoard[m][f] += 60;
                }
            }
        }

        return scoreBoard;
    }

    @Override
    public int[][] calculateInterestScore(int[][] scoreBoard,
                                          Map<Long, Integer> maleUserIdIndex,
                                          Map<Long, Integer> femaleUserIdIndex,
                                          List<UserInterestMatchCandidate> maleUserInterestMatchCandidates,
                                          List<UserInterestMatchCandidate> femaleUserInterestMatchCandidates) {
        Set<UserInterestMatchCandidate> maleUserSet = new HashSet<>(maleUserInterestMatchCandidates);      // 중복 제거를 위해, 남성 사용자: List -> Set으로 변환한다.
        Set<UserInterestMatchCandidate> femaleUserSet = new HashSet<>(femaleUserInterestMatchCandidates);  // 중복 제거를 위해, 여성 사용자: List -> Set으로 변환한다.

        for (UserInterestMatchCandidate m : maleUserSet) {
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserInterestMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // Case 2(데이터 수정)를 위한 장치

                if (m.interestId().equals(f.interestId())) {    // 관심사가 서로 같은가?
                    scoreBoard[mIndex][fIndex] += 30;       // 관심사 매칭 점수 부여(중첩)
                }
            }
        }

        return scoreBoard;
    }

    @Override
    public int[][] calculateMovieGenreScore(int[][] scoreBoard,
                                            Map<Long, Integer> maleUserIdIndex,
                                            Map<Long, Integer> femaleUserIdIndex,
                                            List<UserMovieGenreMatchCandidate> maleUserMovieGenreMatchCandidates,
                                            List<UserMovieGenreMatchCandidate> femaleUserMovieGenreMatchCandidates) {
        Set<UserMovieGenreMatchCandidate> maleUserSet = new HashSet<>(maleUserMovieGenreMatchCandidates);        // 중복 제거를 위해, 남성 사용자: List -> Set으로 변환한다.
        Set<UserMovieGenreMatchCandidate> femaleUserSet = new HashSet<>(femaleUserMovieGenreMatchCandidates);    // 중복 제거를 위해, 여성 사용자: List -> Set으로 변환한다.

        boolean[][] isVisit = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];    // scoreBoard 방문여부 체크

        for (UserMovieGenreMatchCandidate m : maleUserSet) {
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserMovieGenreMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // Case 2(데이터 수정)를 위한 장치

                if (m.movieGenreId().equals(f.movieGenreId())) {    // 영화 장르가 서로 같은가?
                    scoreBoard[mIndex][fIndex] += 10;           // 영화 매칭 점수 부여(중첩)
                }
            }
        }

        return scoreBoard;
    }
}
