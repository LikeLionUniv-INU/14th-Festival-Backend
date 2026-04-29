package com.likelion.zooting.domain.user.dto;

import com.likelion.zooting.domain.user.entity.User;
import java.util.List;

public record UserResponse(
    Long userId,
    String gender,
    String animalTypeName
) {
  public static UserResponse from(User user) {
    // record 생성자에 정의된 순서대로 정확히 값을 전달해야 합니다.
    return new UserResponse(
        user.getUserId(),
        user.getGender() != null ? user.getGender().name() : null,
        user.getAnimalType() != null ? user.getAnimalType().getAnimalName() : null
    );
  }
}
