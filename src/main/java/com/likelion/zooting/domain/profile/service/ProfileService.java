package com.likelion.zooting.domain.profile.service;

import com.likelion.zooting.domain.onboarding.exception.OnboardingErrorCode;
import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.exception.UserErrorCode;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final UserInterestRepository userInterestRepository; // Repository 주입 필요

  @Transactional(readOnly = true)
  public ProfileResponse getProfile(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

    if (user.getAnimalType() == null) {
      throw new GeneralException(OnboardingErrorCode.ONBOARDING_NOT_FOUND);
    }

    // 1. UserInterestRepository를 통해 사용자의 관심사 목록 직접 조회
    List<UserInterest> userInterests = userInterestRepository.findAllByUser(user);

    // 2. 무작위 수식어 추출 (사용자가 선택한 3개 중 1개)
    String modifier = "멋진";
    if (!userInterests.isEmpty()) {
      int randomIndex = (int) (Math.random() * userInterests.size());
      String dbTag = userInterests.get(randomIndex).getInterest().getTag();
      if (dbTag != null && !dbTag.isEmpty()) {
        modifier = dbTag;
      }
    }

    String profileTag = modifier + " " + user.getAnimalType().getAnimalName();

    return ProfileResponse.builder()
        .profileTag(profileTag)
        .animalType(user.getAnimalType().getAnimalName())
        .releaseTime("18:00")
        .releaseMessage("매칭 결과는 18시에 공개됩니다!")
        .build();
  }
}