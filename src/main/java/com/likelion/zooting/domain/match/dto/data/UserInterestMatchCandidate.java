package com.likelion.zooting.domain.match.dto.data;

/**
 * 사용자와 관심사의 매칭을 위한 가공 데이터
 *
 * @param userId
 * @param interestId
 */
public record UserInterestMatchCandidate(
        Long userId,    // 사용자
        Long interestId     // 정확한 중복 필터 로직을 위한 관심사 아이디
) {
}
