package com.likelion.zooting.domain.match.repository.data;

public record UserInterestMatchCandidate(
        Long userId,    // 사용자
        Long interestId     // 정확한 중복 필터 로직을 위한 관심사 아이디
) {
}
