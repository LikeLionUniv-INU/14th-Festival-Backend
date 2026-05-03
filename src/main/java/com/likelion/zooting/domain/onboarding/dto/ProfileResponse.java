package com.likelion.zooting.domain.onboarding.dto;

import lombok.Builder;

@Builder
public record ProfileResponse (
  String profileTag,
  String animalType,
  String releaseTime,
  String releaseMessage
){}
