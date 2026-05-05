package com.likelion.zooting.domain.match.repository;

import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long> {

    boolean existsByMaleUser_GenderAndMaleUser_Status(Gender gender, Status status);

    // 사용자 ID가 남성/여성 사용자 중 하나로 포함된 매칭 결과를 조회한다.
    // 상대방 User까지 함께 로딩하여 partnerInstagramId 조회 시 추가 쿼리를 줄인다.
    @Query("""
        select m
        from Matches m
        join fetch m.maleUser
        join fetch m.femaleUser
        where m.maleUser.userId = :userId
           or m.femaleUser.userId = :userId
    """)
    Optional<Matches> findByUserIdWithUsers(@Param("userId") Long userId);
}