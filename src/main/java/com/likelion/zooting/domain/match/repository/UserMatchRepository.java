package com.likelion.zooting.domain.match.repository;

import com.likelion.zooting.domain.match.dto.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMatchRepository extends JpaRepository<User, Long> {
    /**
     * 매칭 대상자 선별을 위해 조건에 부합하는 사용자 ID 목록을 조회합니다.
     *
     * <p>성별과 제출 상태를 기준으로 필터링된 사용자 ID(Long) 리스트를 반환합니다.</p>
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 필터링된 사용자 ID 리스트
     */
    @Query("SELECT u.userId FROM User u WHERE u.gender = :gender AND u.status = :status")
    List<Long> findByGenderAndStatus(@Param("gender") Gender gender, @Param("status") Status status);

    /**
     * 매칭 대상자 선별을 위해 동물상 데이터를 포함한 사용자 정보를 조회합니다.
     *
     * <p>성별과 제출 상태를 기준으로 필터링하며, 사용자 ID, 본인 동물상, 선호 동물상을
     * {@link UserAnimalMatchCandidate} 객체로 묶어 리스트로 반환합니다.</p>
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정된 조건의 {@link UserAnimalMatchCandidate} 리스트
     */
    @Query("""
            SELECT new com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate(
                        u.userId,
                        u.animalType.animalTypeId,
                        upat.animalType.animalTypeId
                        )
            FROM User u INNER JOIN UserPreferredAnimalType upat ON u.userId = upat.user.userId
            WHERE u.gender = :gender AND u.status = :status
            """)
    List<UserAnimalMatchCandidate> findUserAnimalMatchCandidates(@Param("gender") Gender gender, @Param("status") Status status);

    /**
     * 매칭 대상자 선별을 위해 관심사 데이터를 포함한 사용자 정보를 조회합니다.
     *
     * <p>성별과 제출 상태를 기준으로 필터링하며, 사용자 ID와 관심사 ID를
     * {@link UserInterestMatchCandidate} 객체로 묶어 리스트로 반환합니다.</p>
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정된 조건의 {@link UserInterestMatchCandidate} 리스트
     */
    @Query("""
            SELECT new com.likelion.zooting.domain.match.repository.data.UserInterestMatchCandidate(
                        u.userId,
                        ui.interest.interestId
                        )
            FROM User u INNER JOIN UserInterest ui ON u.userId = ui.user.userId
            WHERE u.gender = :gender AND u.status = :status
            """)
    List<UserInterestMatchCandidate> findUserInterestMatchCandidates(@Param("gender") Gender gender, @Param("status") Status status);

    /**
     * 매칭 대상자 선별을 위해 영화 장르 데이터를 포함한 사용자 정보를 조회합니다.
     *
     * <p>성별과 제출 상태를 기준으로 필터링하며, 사용자 ID와 선호 영화 장르 ID를
     * {@link UserMovieGenreMatchCandidate} 객체로 묶어 리스트로 반환합니다.</p>
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정된 조건의 {@link UserMovieGenreMatchCandidate} 리스트
     */
    @Query("""
            SELECT new com.likelion.zooting.domain.match.repository.data.UserMovieGenreMatchCandidate(
                        u.userId,
                        umg.movieGenre.movieGenreId
                        )
            FROM User u INNER JOIN UserMovieGenre umg ON u.userId = umg.user.userId
            WHERE u.gender = :gender AND u.status = :status
            """)
    List<UserMovieGenreMatchCandidate> findUserMovieGenreMatchCandidate(@Param("gender") Gender gender, @Param("status") Status status);
}
