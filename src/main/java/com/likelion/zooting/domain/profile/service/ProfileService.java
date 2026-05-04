package com.likelion.zooting.domain.profile.service;

import com.likelion.zooting.domain.interest.entity.Interest;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

  private static final String RELEASE_TIME = "18:00";
  private static final String RELEASE_MESSAGE = "매칭 결과는 18시에 공개됩니다.";

  private final UserRepository userRepository;
  private final UserInterestRepository userInterestRepository;

  public ProfileResponse getProfile(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(UserErrorCode.PROFILE_NOT_FOUND));

    if (user.getAnimalType() == null) {
      throw new GeneralException(UserErrorCode.PROFILE_NOT_FOUND);
    }

    List<UserInterest> userInterests = userInterestRepository.findAllByUserIdWithInterest(userId);

    if (userInterests.isEmpty()) {
      throw new GeneralException(UserErrorCode.PROFILE_NOT_FOUND);
    }

    String animalName = user.getAnimalType().getAnimalName();
    String interestTag = getRepresentativeInterestTag(userInterests);

    return new ProfileResponse(
        createProfileTag(interestTag, animalName),
        convertAnimalTypeToCode(animalName),
        RELEASE_TIME,
        RELEASE_MESSAGE
    );
  }

  private String getRepresentativeInterestTag(List<UserInterest> userInterests) {
    return userInterests.stream()
        .map(UserInterest::getInterest)
        .filter(interest -> interest != null && interest.getTag() != null)
        .map(Interest::getTag)
        .findFirst()
        .orElseThrow(() -> new GeneralException(UserErrorCode.PROFILE_NOT_FOUND));
  }

  // 관심사 태그 + 동물상으로 프로필 태그 생성
  private String createProfileTag(String interestTag, String animalName) {
    return interestTag + " " + animalName;
  }

  private String convertAnimalTypeToCode(String animalName) {
    return switch (animalName) {
      case "토끼" -> "rabbit";
      case "강아지" -> "dog";
      case "고양이" -> "cat";
      case "여우" -> "fox";
      case "곰" -> "bear";
      case "공룡" -> "dinosaur";
      case "햄스터" -> "hamster";
      case "늑대" -> "wolf";
      case "사슴" -> "deer";
      default -> animalName;
    };
  }
}