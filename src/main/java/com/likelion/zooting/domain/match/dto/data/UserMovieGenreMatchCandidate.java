package com.likelion.zooting.domain.match.dto.data;

/**
 * 사용자와 영화 장르 매칭을 위한 가공 데이터
 *
 * @param userId
 * @param movieGenreId
 */
public record UserMovieGenreMatchCandidate(
        Long userId,        // 사용자 ID
        Long movieGenreId   // 영화 장르 ID
) {
}
