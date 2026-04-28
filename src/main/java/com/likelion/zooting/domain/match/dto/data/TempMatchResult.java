package com.likelion.zooting.domain.match.dto.data;

import java.util.List;

/**
 * 매칭된 결과를 담은 임시 데이터
 *
 * @param totalUserCount       매칭에 적용된 전체 사용자 수
 * @param matchedPairCount     매칭된 짝의 개수
 * @param unmatchedUserCount   매칭에서 제외된 사용자 수
 * @param finalMatchedPairList 최종 매칭 쌍 리스트
 */
public record TempMatchResult(
        Integer totalUserCount,
        Integer matchedPairCount,
        Integer unmatchedUserCount,
        List<TempMatch> finalMatchedPairList
) {
}
