package com.likelion.zooting.domain.match.dto.data;

/**
 * 사용자와 동물상과의 매칭을 위한 가공데이터
 *
 * @param userId
 * @param animalTypeId
 * @param preferredAnimalTypeId
 */
public record UserAnimalMatchCandidate(
        Long userId,
        Long animalTypeId,
        Long preferredAnimalTypeId
) {
}
