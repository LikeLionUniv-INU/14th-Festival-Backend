package com.likelion.zooting.domain.match.repository.data;

import java.util.List;

public record TempMatchResult(
        Integer totalUserCount,     // 매칭에 적용된 전체 사용자 수
        Integer matchedPairCount,   // 매칭된 짝의 개수
        Integer unmatchedUserCount,  // 매칭에서 제외된 사용자 수
        List<TempMatch> finalMatchedPairList    // 최종 매칭 쌍 리스트
) {
}
