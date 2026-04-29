package com.likelion.zooting.domain.animaltype.repository;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {
}
