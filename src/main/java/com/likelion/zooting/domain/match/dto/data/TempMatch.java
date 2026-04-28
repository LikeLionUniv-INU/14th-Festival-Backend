package com.likelion.zooting.domain.match.dto.data;

/**
 * 매칭 로직 전 임시로 남성, 여성 사용자 쌍을 담을 가공 데이터
 *
 * @param maleUserIndex
 * @param femaleUserIndex
 * @param score
 */
public record TempIndexMatch(
        Integer maleUserIndex,      // 남성 사용자의 index 값(점수 매칭 보드 scoreBoard의 row)
        Integer femaleUserIndex,    // 여성 사용자의 index 값(점수 매칭 보드 scoreBoard의 column)
        Integer score               // 두 관계 간 최종 매칭 점수
) {
}
