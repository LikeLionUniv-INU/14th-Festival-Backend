package com.likelion.zooting.domain.user.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@AllArgsConstructor
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 ANIMAL_TYPE을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "animal_type_id")    // 외래키 설정
    private AnimalType animalTypeId;

    @Column(name = "instagramId", length = 50)
    private String instagramId;

    @Column(name = "user_pw", length = 255)
    private String UserPw;

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "status", length = 10)
    private Status status;

    @Column(name = "is_complete", nullable = false) // null값 금지
    private boolean isComplete;

    @Column(name = "created_at", updatable = false) // 수정 못하게 설정
    private LocalDateTime createdAt;
}
