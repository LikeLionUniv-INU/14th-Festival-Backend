package com.likelion.zooting.domain.profile.service;

import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
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

  private static final Map<String, String> MODIFIER_MAP = Map.of(
      "스포츠", "스포츠광",
      "뮤지컬/연극", "뮤덕",
      "반려동물", "집사",
      "여행", "탐험하는",
      "맛집탐방", "맛잘알",
      "자기계발", "갓생러",
      "덕질", "마니아",
      "음악감상", "음잘알",
      "게임", "게이머"
  );

  @Transactional(readOnly = true)
  public ProfileResponse getProfile(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    if (user.getAnimalType() == null) {
      throw new IllegalStateException("온보딩 정보가 존재하지 않습니다.");
    }

    // 1. UserInterestRepository를 통해 사용자의 관심사 목록 직접 조회
    List<UserInterest> userInterests = userInterestRepository.findAllByUser(user);

    // 2. 무작위 수식어 추출 (사용자가 선택한 3개 중 1개)
    String modifier = "멋진";
    if (!userInterests.isEmpty()) {
      int randomIndex = (int) (Math.random() * userInterests.size());
      String interestName = userInterests.get(randomIndex).getInterest().getInterestName();
      modifier = MODIFIER_MAP.getOrDefault(interestName, "멋진");
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