package com.likelion.zooting.domain.match.service.data;

/**
 * 매칭 로직 전 임시로 남성, 여성 사용자 쌍을 담을 가공 데이터
 *
 * @param maleUserIndex   남성 사용자의 index 값(점수 매칭 보드 scoreBoard의 row)
 * @param femaleUserIndex 여성 사용자의 index 값(점수 매칭 보드 scoreBoard의 column)
 * @param score           두 관계 간 최종 매칭 점수
 */
public record TempMatch(
        Integer maleUserIndex,
        Integer femaleUserIndex,
        Integer score
) {
}
