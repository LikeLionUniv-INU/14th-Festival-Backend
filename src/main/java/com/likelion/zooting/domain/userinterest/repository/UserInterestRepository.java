package com.likelion.zooting.domain.userinterest.repository;

import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest,Long> {

  // 사용자 ID를 기준으로 '선택한 관심사 매핑 정보'를 조회하면서,
  // 연관된 Interest 엔티티를 함께 로딩하여 관심사 태그를 바로 사용할 수 있도록 한다.
  @Query("""
        select ui
        from UserInterest ui
        join fetch ui.interest
        where ui.user.userId = :userId
    """)
  List<UserInterest> findAllByUserIdWithInterest(@Param("userId") Long userId);
}
