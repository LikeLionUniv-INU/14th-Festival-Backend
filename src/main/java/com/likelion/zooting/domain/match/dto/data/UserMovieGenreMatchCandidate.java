package com.likelion.zooting.domain.match.dto.data;

/**
 * 사용자와 영화 장르 매칭을 위한 가공 데이터
 *
 * @param userId       사용자 ID
 * @param movieGenreId 해당 사용자의 영화 장르 ID
 */
public record UserMovieGenreMatchCandidate(
        Long userId,
        Long movieGenreId
) {
}
