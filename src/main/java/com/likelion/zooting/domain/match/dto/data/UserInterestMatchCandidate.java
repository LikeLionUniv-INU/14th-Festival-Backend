package com.likelion.zooting.domain.match.dto.data;

/**
 * 사용자와 관심사의 매칭을 위한 가공 데이터
 *
 * @param userId     사용자 ID
 * @param interestId 해당 사용자의 관심사 ID
 */
public record UserInterestMatchCandidate(
        Long userId,
        Long interestId
) {
}
