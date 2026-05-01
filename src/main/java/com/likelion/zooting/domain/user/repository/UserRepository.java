package com.likelion.zooting.domain.user.repository;

import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    List<User> findAllByGender(Gender gender);
    List<User> findByGenderAndStatus(Gender gender, Status status);
    Optional<User> findByInstagramId(String instagramId);
}
