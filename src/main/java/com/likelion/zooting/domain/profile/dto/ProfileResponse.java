package com.likelion.zooting.domain.profile.dto;

public record ProfileResponse(
    String profileTag,
    String animalType,
    String releaseTime,
    String releaseMessage
) {
}
