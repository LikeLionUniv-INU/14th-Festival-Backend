package com.likelion.zooting.domain.match.repository.data;

public record TempMatch(
        Integer maleUserIndex,      // 남성 사용자의 index 값(점수 매칭 보드 scoreBoard의 row)
        Integer femaleUserIndex,    // 여성 사용자의 index 값(점수 매칭 보드 scoreBoard의 column)
        Integer score               // 두 관계 간 최종 매칭 점수
) {
}
