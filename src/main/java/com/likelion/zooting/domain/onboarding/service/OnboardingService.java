package com.likelion.zooting.domain.onboarding.service;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import com.likelion.zooting.domain.animaltype.entity.Scope;
import com.likelion.zooting.domain.animaltype.repository.AnimalTypeRepository;
import com.likelion.zooting.domain.interest.entity.Interest;
import com.likelion.zooting.domain.interest.repository.InterestRepository;
import com.likelion.zooting.domain.moviegenre.entity.MovieGenre;
import com.likelion.zooting.domain.moviegenre.repository.MovieGenreRepository;
import com.likelion.zooting.domain.onboarding.dto.OnboardingRequest;
import com.likelion.zooting.domain.onboarding.exception.OnboardingErrorCode;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.exception.UserErrorCode;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.userinterest.repository.UserInterestRepository;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.usermoviegenre.repository.UserMovieGenreRepository;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import com.likelion.zooting.domain.userpreferredanimaltype.repository.UserPreferredAnimalTypeRepository;
import com.likelion.zooting.global.exception.GeneralException;
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
        .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

    // 2. 상태 검증 - IN_PROGRESS 상태에서만 제출 허용
    if (user.getStatus() != Status.IN_PROGRESS) {
      throw new GeneralException(UserErrorCode.ALREADY_COMPLETED);
    }

    // 3. 성별 검증
    if (request.gender() == null || request.gender().isBlank()) {
      throw new GeneralException(OnboardingErrorCode.GENDER_REQUIRED);
    }

    Gender gender = Gender.valueOf(request.gender().toUpperCase());

    // 4. 본인 동물상 조회
    String myAnimalName = request.animalType();

    AnimalType myAnimal = animalTypeRepository.findByAnimalName(myAnimalName)
            .orElseThrow(() -> new GeneralException(OnboardingErrorCode.ANIMAL_TYPE_NOT_FOUND));

    // 5. User 정보 업데이트 (성별 + 본인 동물상)
    user.updateOnboarding(gender, myAnimal);

    // 6. 선호 동물상 저장 로직
    List<String> preferredList = request.preferredAnimals();

    if (preferredList == null || preferredList.isEmpty()) {
      throw new GeneralException(OnboardingErrorCode.ANIMAL_TYPE_COUNT_INVALID);
    }

    // "상관없음" 선택 여부 확인
    boolean isIndifferent = preferredList.size() == 1 && "상관없음".equals(preferredList.get(0));

    if (isIndifferent) {
      // 성별에 따른 모든 가능한 동물상 조회 (COMMON + 사용자 성별 전용)
      // 예: MALE일 경우 COMMON(강아지, 고양이, 햄스터) + MALE(곰, 원숭이, 공룡)
      List<AnimalType> allPossibleAnimals = animalTypeRepository.findByScopeIn(
              List.of(Scope.COMMON, Scope.valueOf(user.getGender().name()))
      );

      for (AnimalType animalType : allPossibleAnimals) {
        preferredAnimalRepository.save(new UserPreferredAnimalType(user, animalType));
      }

    } else if (preferredList.size() == 3) {
      // 일반적인 3개 선택 로직
      for (String animalName : preferredList) {
        AnimalType animalType = animalTypeRepository.findByAnimalName(animalName)
            .orElseThrow(() -> new GeneralException(OnboardingErrorCode.ANIMAL_TYPE_NOT_FOUND));

        preferredAnimalRepository.save(new UserPreferredAnimalType(user, animalType));
      }

    } else {
      // 1개(상관없음 아님)나 2개 등을 선택한 경우 에러 처리
      throw new GeneralException(OnboardingErrorCode.ANIMAL_TYPE_COUNT_INVALID);
    }

    // 7. 관심사 검증 및 저장
    List<String> interestList = request.interests();

    if (interestList == null || interestList.size() != 3) {
      throw new GeneralException(OnboardingErrorCode.INTEREST_COUNT_INVALID);
    }

    for (String interestName : interestList) {
      String cleanedInterest = interestName.replace("#", "").trim();

      Interest interest = interestRepository.findByInterestName(cleanedInterest)
              .orElseThrow(() -> new GeneralException(OnboardingErrorCode.INTEREST_NOT_FOUND));

      UserInterest userInterest = new UserInterest(user, interest);
      userInterestRepository.save(userInterest);
    }

    // 8. 영화 장르 검증 및 저장
    List<String> movieGenreList = request.movieGenres();

    if (movieGenreList == null || movieGenreList.size() != 2) {
      throw new GeneralException(OnboardingErrorCode.MOVIE_GENRE_COUNT_INVALID);
    }

    for (String genreName : movieGenreList) {
      String cleanedGenre = genreName.replace("#", "").trim();

      MovieGenre movieGenre = movieGenreRepository.findByMovieGenreName(cleanedGenre)
              .orElseThrow(() -> new GeneralException(OnboardingErrorCode.MOVIE_GENRE_NOT_FOUND));

      UserMovieGenre userMovieGenre = new UserMovieGenre(user, movieGenre);
      userMovieGenreRepository.save(userMovieGenre);
    }

    // 9. 상태 전이 - IN_PROGRESS → SUBMITTED
    user.markSubmitted();

    // 10. 결과 반환
    return true;
  }
}