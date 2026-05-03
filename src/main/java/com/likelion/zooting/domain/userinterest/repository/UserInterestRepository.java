package com.likelion.zooting.domain.userinterest.repository;

import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
  List<UserInterest> findAllByUser(User user);
}