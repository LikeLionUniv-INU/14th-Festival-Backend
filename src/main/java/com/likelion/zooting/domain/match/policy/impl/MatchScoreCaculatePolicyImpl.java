package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.policy.MatchScoreCalculatePolicy;
import com.likelion.zooting.domain.match.repository.UserMatchRepository;
import com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserMovieGenreMatchCandidate;
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
public class MatchScoreCaculatePolicyImpl implements MatchScoreCalculatePolicy {

    private final UserMatchRepository userMatchRepository;

    @Override
    public int[][] calculateAnimalTypeScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
        List<UserAnimalMatchCandidate> maleUser = userMatchRepository.findUserAnimalMatchCandidates(Gender.MALE, Status.SUBMITTED);      // 제출한 남성 사용자의 id와 동물상, 선호하는 동물상 데이터를 가져온다.
        List<UserAnimalMatchCandidate> femaleUser = userMatchRepository.findUserAnimalMatchCandidates(Gender.FEMALE, Status.SUBMITTED);    // 제출한 여성 사용자의 id와 동물상, 선호하는 동물상 데이터를 가져온다.
        boolean[][] maleToFemale = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];   // 동물상(남성) -> 선호 동물상(여성)
        boolean[][] femaleToMale = new boolean[maleUserIdIndex.size()][femaleUserIdIndex.size()];   // 동물상(여성) -> 선호 동물상(남성)

        if (maleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.MALE_USER_ANIMAL_MATCH_CANDIDATE);    // 동물상 계산을 위한 남성 사용자 검증
        if (femaleUser.isEmpty())
            throw new GeneralException(MatchErrorCode.FEMALE_USER_ANIMAL_MATCH_CANDIDATE); // 동물상 계산을 위한 여성 사용자 검증

        for (UserAnimalMatchCandidate m : maleUser) {
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserAnimalMatchCandidate f : femaleUser) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치
                if (m.animalTypeId().equals(f.preferredAnimalTypeId()) ||       // 남성 사용자의 동물상 = 여성 사용자의 선호하는 동물상 OR
                        m.preferredAnimalTypeId().equals(f.animalTypeId())) {   // 여성 사용자의 동물상 = 남성 사용자의 선호하는 동물상
                    Integer mIndex = maleUserIdIndex.get(m.userId());    // 남성 사용자 ID의 index값
                    Integer fIndex = femaleUserIdIndex.get(f.userId());  // 여성 사용자 ID의 index값

                    if (mIndex == null || fIndex == null) continue;   // Case 2(데이터 수정)를 위한 장치

                    if(m.animalTypeId() == f.preferredAnimalTypeId()){  // 동물상(남성) == 선호 동물상(여성)
                        maleToFemale[mIndex][fIndex] = true;
                    }
                    if(m.preferredAnimalTypeId() == f.animalTypeId()){  // 동물상(남성) == 선호 동물상(여성)
                        femaleToMale[fIndex][mIndex] = true;
                    }
                }
            }
        }

        // Case 3(점수의 원소성) 보장을 위한 장치
        for(int m = 0; m < maleUserIdIndex.size(); m++){
            for(int f = 0; f < femaleUserIdIndex.size(); f++){
                if(maleToFemale[m][f] && femaleToMale[m][f]){  // 서로 상대의 선호하는 동물상과 자신의 동물상이 일치하는가? (양방향성 확인)
                    scoreBoard[m][f] += 60;
                }
            }
        }

        return scoreBoard;
    }

    @Override
    public int[][] calculateInterestScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
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
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserInterestMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // Case 2(데이터 수정)를 위한 장치

                if (m.interestId().equals(f.interestId())) {    // 관심사가 서로 같은가?
                    if (scoreBoard[mIndex][fIndex] > 0) {       // 매칭 필터(동물상 1개 이상 선택)를 통과 했는가?
                        scoreBoard[mIndex][fIndex] += 10;       // 관심사 매칭 점수 부여(중첩)
                        isVisit[mIndex][fIndex] = true;         // 방문 체크
                    }
                }
            }
        }

        for (int m = 0; m < isVisit.length; m++) {
            for (int f = 0; f < isVisit[m].length; f++) {
                if (!isVisit[m][f]) {           // 관심사 하나라도 선택 안했는가?
                    scoreBoard[m][f] = 0;       // 점수 0점 처리
                }
            }
        }

        return scoreBoard;
    }

    @Override
    public int[][] calculateMovieGenreScore(int[][] scoreBoard, Map<Long, Integer> maleUserIdIndex, Map<Long, Integer> femaleUserIdIndex) {
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
            if (m == null) continue;        // Case 1(데이터 신규 추가)를 위한 장치
            for (UserMovieGenreMatchCandidate f : femaleUserSet) {
                if (f == null) continue;    // Case 1(데이터 신규 추가)를 위한 장치

                Integer mIndex = maleUserIdIndex.get(m.userId());
                Integer fIndex = femaleUserIdIndex.get(f.userId());
                if (mIndex == null || fIndex == null) continue; // Case 2(데이터 수정)를 위한 장치

                if (m.movieGenreId().equals(f.movieGenreId())) {    // 영화 장르가 서로 같은가?
                    if (scoreBoard[mIndex][fIndex] > 0) {           // 매칭 필터(동물상, 관심사 1개 이상 선택)를 통과 했는가?
                        scoreBoard[mIndex][fIndex] += 10;           // 관심사 매칭 점수 부여(중첩)
                        isVisit[mIndex][fIndex] = true;             // 방문 체크
                    }
                }
            }
        }

        for (int m = 0; m < isVisit.length; m++) {
            for (int f = 0; f < isVisit[m].length; f++) {
                if (!isVisit[m][f]) {           // 영화 장르 하나라도 선택 안했는가?
                    scoreBoard[m][f] = 0;       // 점수 0점 처리
                }
            }
        }

        return scoreBoard;
    }
}
