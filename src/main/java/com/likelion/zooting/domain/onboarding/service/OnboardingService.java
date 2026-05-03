package com.likelion.zooting.domain.onboarding.service;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import com.likelion.zooting.domain.animaltype.repository.AnimalTypeRepository;
import com.likelion.zooting.domain.interest.entity.Interest;
import com.likelion.zooting.domain.interest.repository.InterestRepository;
import com.likelion.zooting.domain.moviegenre.entity.MovieGenre;
import com.likelion.zooting.domain.moviegenre.repository.MovieGenreRepository;
import com.likelion.zooting.domain.onboarding.dto.OnboardingRequest;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.usermoviegenre.repository.UserMovieGenreRepository;
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
  private final InterestRepository interestRepository;
  private final UserInterestRepository userInterestRepository;
  private final MovieGenreRepository movieGenreRepository;
  private final UserMovieGenreRepository userMovieGenreRepository;

  @Transactional
  public boolean submitOnboarding(Long userId, OnboardingRequest request) {
    // 1. 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 2. 상태 검증 - IN_PROGRESS 상태에서만 제출 허용
    if (user.getStatus() != Status.IN_PROGRESS) {
      throw new IllegalStateException("이미 온보딩을 완료한 사용자입니다.");
    }

    // 3. 본인 동물상 조회
    String myAnimalName = request.animalType();

    AnimalType myAnimal = animalTypeRepository.findByAnimalName(myAnimalName)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동물상: " + myAnimalName));

    // 4. User 정보 업데이트 (성별 + 본인 동물상)
    user.updateOnboarding(Gender.valueOf(request.gender().toUpperCase()), myAnimal);

    // 5. 선호 동물상 저장
    List<String> preferredList = request.preferredAnimals();

    for (String animalName : preferredList) {

      AnimalType animalType = animalTypeRepository.findByAnimalName(animalName)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동물상: " + animalName));

      // 생성자 호출
      UserPreferredAnimalType preferredAnimal = new UserPreferredAnimalType(user, animalType);

      // 저장
      preferredAnimalRepository.save(preferredAnimal);
    }

    // 6. 관심사 저장
    List<String> interestList = request.interests();

    for (String interestName : interestList) {
      String cleanedInterest = interestName.replace("#", "").trim();

      Interest interest = interestRepository.findByInterestName(cleanedInterest)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관심사: " + interestName));

      // 생성자 호출
      UserInterest userInterest = new UserInterest(user, interest);

      // 저장
      userInterestRepository.save(userInterest);
    }

    // 7. 영화 장르 저장
    List<String> movieGenreList = request.movieGenres();

    for (String genreName : movieGenreList) {
      String cleanedGenre = genreName.replace("#", "").trim();

      MovieGenre movieGenre = movieGenreRepository.findByMovieGenreName(cleanedGenre)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화 장르: " + genreName));

      // 생성자 호출
      UserMovieGenre userMovieGenre = new UserMovieGenre(user, movieGenre);

      // 저장
      userMovieGenreRepository.save(userMovieGenre);
    }

    // 8. 상태 전이 - IN_PROGRESS → SUBMITTED
    user.markSubmitted();

    // 9. 결과 반환
    return true;
  }
}
