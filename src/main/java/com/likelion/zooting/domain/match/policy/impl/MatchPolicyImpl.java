package com.likelion.zooting.domain.match.policy.impl;

import com.likelion.zooting.domain.match.policy.MatchPolicy;
import com.likelion.zooting.domain.match.repository.data.TempMatch;
import com.likelion.zooting.domain.match.repository.data.TempMatchResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class MatchPolicyImpl implements MatchPolicy {
    @Override
    public TempMatchResult simulateMatching(int[][] scoreBoard, Map<Integer, Long> indexToMaleUserId, Map<Integer, Long> indexToFemaleUserId) {
        Integer totalUserCount = indexToFemaleUserId.size() + indexToMaleUserId.size();
        Integer matchedPairCount = 0;
        Integer unmatchedUserCount = 0;

        // 모든 매칭을 나열
        List<TempMatch> relations = new ArrayList<>();

        for (int m = 0; m < scoreBoard.length; m++) {
            for (int f = 0; f < scoreBoard[m].length; f++) {
                // 필터 적용
                if (scoreBoard[m][f] == 0) {
                    continue;
                }
                relations.add(new TempMatch(m, f, scoreBoard[m][f]));
            }
        }

        // 매칭 점수가 높은 순으로, 내림차순 정렬
        relations.sort(Comparator.comparingInt(TempMatch::score).reversed());

        // 방문 여부를 체크하기 위한 배열
        boolean[] isVisitMaleUser = new boolean[indexToMaleUserId.size()];
        boolean[] isVisitFemaleUser = new boolean[indexToFemaleUserId.size()];

        // 그리디 알고리즘
        for (TempMatch tm : relations) {
            if (!isVisitMaleUser[tm.maleUserIndex()] && !isVisitFemaleUser[tm.femaleUserIndex()]) {
                isVisitMaleUser[tm.maleUserIndex()] = true;
                isVisitFemaleUser[tm.femaleUserIndex()] = true;
                matchedPairCount++;
            }
        }

        // 매칭되지 않는 사용자 수
        unmatchedUserCount += (int) IntStream.range(0, isVisitMaleUser.length).filter(i -> !isVisitMaleUser[i]).count();
        unmatchedUserCount += (int) IntStream.range(0, isVisitFemaleUser.length).filter(i -> !isVisitFemaleUser[i]).count();

        return new TempMatchResult(totalUserCount, matchedPairCount, unmatchedUserCount);
    }
}
