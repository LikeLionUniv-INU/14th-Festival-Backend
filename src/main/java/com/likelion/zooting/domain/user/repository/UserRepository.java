package com.likelion.zooting.domain.user.repository;

import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.userId FROM User u WHERE u.gender = :gender AND u.status = :status")
    List<Long> findByGenderAndStatus(@Param("gender") Gender gender, @Param("status") Status status);
}
