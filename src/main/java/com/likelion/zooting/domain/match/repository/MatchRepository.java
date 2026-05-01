package com.likelion.zooting.domain.match.repository;

import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long> {
    boolean existsByMaleUser_GenderAndMaleUser_Status(Gender gender, Status status);
}
