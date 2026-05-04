package com.likelion.zooting.domain.animaltype.service.converter;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AnimalTypeIdToName {
    public Map<Long, String> animalTypesToNames(List<AnimalType> animalTypes) {
        return animalTypes.stream()
                .collect(Collectors.toMap(
                        AnimalType::getAnimalTypeId,
                        AnimalType::getAnimalName
                ));
    }
}
