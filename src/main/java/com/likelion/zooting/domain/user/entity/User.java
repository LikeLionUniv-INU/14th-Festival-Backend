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

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 동물상을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "animal_type_id")    // 외래키 설정
    private AnimalType animalType;

    @Column(name = "instagramId", length = 50)
    private String instagramId;

    @Column(name = "user_pw", length = 255)
    private String UserPw;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "is_complete", nullable = false) // null값 금지
    private boolean isComplete;

    @Column(name = "created_at", updatable = false) // 수정 못하게 설정
    private LocalDateTime createdAt;
}
