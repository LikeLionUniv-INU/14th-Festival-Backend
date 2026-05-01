package com.likelion.zooting.domain.userpreferredanimaltype.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자
@Table(name = "USER_ANIMAL_TYPE")
public class UserPreferredAnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ANIMAL_TYPE_ID")
    private Long userAnimalTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANIMAL_TYPE_ID")
    private AnimalType animalType;

    // --- [직접 만든 생성자] ---
    public UserPreferredAnimalType(User user, AnimalType animalType) {
        this.user = user;
        this.animalType = animalType;
    }
}
