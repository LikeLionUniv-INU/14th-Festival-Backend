package com.likelion.zooting.domain.userpreferredanimaltype.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@AllArgsConstructor
@Table(name = "USER_ANIMAL_TYPE")
public class UserAnimalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ANIMAL_TYPE")
    private Long userAnimalTypeId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "user_id")   // 외래키 설정
    private User user;  // DB로 저장될 땐 PK로 저장되지만, Entity로 가져오므로 user로 수정

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 ANIMAL_TYPE을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "animal_type_id")    // 외래키 설정
    private AnimalType animalType;  // DB로 저장될 땐 PK로 저장되지만, Entity로 가져오므로 animalType으로 지었다.

}
