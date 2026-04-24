package com.likelion.zooting.domain.match.repository.data;

public record UserMovieGenreMatchCandidate(
        Long userId,        // 사용자 ID
        Long movieGenreId   // 영화 장르 ID
) {
}
