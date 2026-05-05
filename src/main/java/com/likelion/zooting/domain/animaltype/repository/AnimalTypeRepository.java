package com.likelion.zooting.domain.animaltype.repository;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.likelion.zooting.domain.animaltype.entity.Scope;

import java.util.List;
import java.util.Optional;

public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {
  Optional<AnimalType> findByAnimalName(String animalname);

  List<AnimalType> findByScopeIn(List<Scope> scopes);
}
