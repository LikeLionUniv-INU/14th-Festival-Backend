package com.likelion.zooting.domain.animaltype.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "ANIMAL_TYPE")
public class AnimalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANIMAL_TYPE_ID")
    private Long animalTypeId;

    @Column(name = "ANIMAL_NAME", length = 30)
    private String animalName;  // 동물상 이름

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "SCOPE", length = 30)
    private Scope scope;   // 공통, 남성, 여성에 따라 적용(+ 유연한 적용 범위 확장)

}
