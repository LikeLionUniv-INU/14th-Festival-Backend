package com.likelion.zooting.domain.onboarding.service;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import com.likelion.zooting.domain.animaltype.repository.AnimalTypeRepository;
import com.likelion.zooting.domain.onboarding.dto.OnboardingRequest;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import com.likelion.zooting.domain.userpreferredanimaltype.repository.UserPreferredAnimalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingService {

  private final UserRepository userRepository;
  private final AnimalTypeRepository animalTypeRepository;
  private final UserPreferredAnimalTypeRepository preferredAnimalRepository;

  @Transactional
  public boolean submitOnboarding(Long userId, OnboardingRequest request) {
    // 1. 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 2. 선호 동물상 저장
    // 주의: OnboardingRequest의 필드명이 preferredAnimals인지 preferredAnimalTypes인지 확인하세요.
    // 만약 에러가 난다면 request.preferredAnimalTypes()로 바꿔야 합니다.
    List<String> preferredList = request.preferredAnimalTypes();

    for (String animalName : preferredList) {
      // AnimalTypeRepository에 findByName 메서드가 반드시 있어야 합니다!
      AnimalType animalType = animalTypeRepository.findByName(animalName)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동물상: " + animalName));

      // 생성자 호출
      UserPreferredAnimalType preferredAnimal = new UserPreferredAnimalType(user, animalType);

      // 저장
      preferredAnimalRepository.save(preferredAnimal);
    }

    // 3. 결과 반환 (Missing return statement 해결)
    return true;
  }
}
