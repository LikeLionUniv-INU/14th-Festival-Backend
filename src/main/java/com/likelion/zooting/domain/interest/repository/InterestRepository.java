package com.likelion.zooting.domain.interest.repository;

import com.likelion.zooting.domain.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest,Long> {
  Optional<Interest> findByInterestName(String interestname);
}
