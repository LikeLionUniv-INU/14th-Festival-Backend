package com.likelion.zooting.domain.match.repository.data;

public record UserAnimalMatchCandidate(
        Long userId,
        Long animalTypeId,
        Long preferredAnimalTypeId
) {
}
