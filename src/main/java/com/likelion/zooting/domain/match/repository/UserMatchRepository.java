package com.likelion.zooting.domain.match.repository;

import com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.repository.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMatchRepository extends JpaRepository<User, Long> {
    /**
     * 매칭 대상자 선별을 위해 User 도메인의 데이터를 조회한다.
     * <p>
     * 성별과 제출 상태를 기준으로 필터링된 사용자 ID 목록을 반환한다
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정된 성별과 상태로 필터링된 사용자 ID의 리스트
     */
    @Query("SELECT u.userId FROM User u WHERE u.gender = :gender AND u.status = :status")
    List<Long> findByGenderAndStatus(@Param("gender") Gender gender, @Param("status") Status status);

    /**
     * 매칭 대상자 선별을 위해 User 도메인의 데이터를 조회한다.
     * <p>
     * 성별과 제출 상태를 기준으로 필터링된 유저 ID 목록을 반환한다.
     * 사용자 ID, 사용자 동물상, 사용자 선호 동물상을 {@link UserAnimalMatchCandidate}로 묶어, 리스트로 내보낸다.
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정한 성별과 상태를 가진 {@link UserAnimalMatchCandidate}리스트
     */
    @Query("""
            SELECT new com.likelion.zooting.domain.match.repository.data.UserAnimalMatchCandidate(
                        u.userId,
                        u.animalType.animalTypeId,
                        uat.animalType.animalTypeId
                        )
            FROM User u INNER JOIN UserAnimalType uat ON u.userId = uat.user.userId
            WHERE u.gender = :gender AND u.status = :status
            """)
    List<UserAnimalMatchCandidate> findUserAnimalMatchCandidates(@Param("gender") Gender gender, @Param("status") Status status);

    /**
     * 매칭 대상자 선별을 위해 User 도메인의 데이터를 조회한다.
     * <p>
     * 성별과 제출 상태를 기준으로 필터링된 유저 ID 목록을 반환한다.
     * 사용자 ID, 사용자 관심사 ID를 {@link UserInterestMatchCandidate}로 묶어, 리스트로 내보낸다.
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정한 성별과 상태를 가진 {@link UserInterestMatchCandidate}리스트
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
     * 매칭 대상자 선별을 위해 User 도메인의 데이터를 조회한다.
     * <p>
     * 성별과 제출 상태를 기준으로 필터링된 유저 ID 목록을 반환한다.
     * 사용자 ID, 사용자 관심사 ID를 {@link UserMovieGenreMatchCandidate}로 묶어, 리스트로 내보낸다.
     *
     * @param gender 필터링할 성별
     * @param status 필터링할 상태
     * @return 지정한 성별과 상태를 가진 {@link UserMovieGenreMatchCandidate}리스트
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
