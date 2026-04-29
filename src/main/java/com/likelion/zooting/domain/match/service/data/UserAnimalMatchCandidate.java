package com.likelion.zooting.domain.match.service.data;

/**
 * 사용자와 동물상과의 매칭을 위한 가공데이터
 *
 * @param userId                사용자 ID
 * @param animalTypeId          해당 사용자의 동물상 ID
 * @param preferredAnimalTypeId 해당 사용자가 선호하는 동물상 ID
 */
public record UserAnimalMatchCandidate(
        Long userId,
        Long animalTypeId,
        Long preferredAnimalTypeId
) {
}
