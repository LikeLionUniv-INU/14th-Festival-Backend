package com.likelion.zooting.domain.user.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용을 위한 전체 생성자
@Builder
@Table(name = "USER")
public class User {
    // 1. 공통 식별자 (기본키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    // 2. 협업자 작성 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANIMAL_TYPE_ID")
    private AnimalType animalType;

    @Column(name = "INSTAGRAM_ID", length = 50)
    private String instagramId;

    @Column(name = "USER_PW", length = 255)
    private String userPw;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10)
    private Status status;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "PRIVACY_CONSENT")
    private boolean privacyConsent;

    // 3. 본인 추가 필드 (온보딩 정보)
    @Column(name = "MY_ANIMAL_TYPE")
    private String myAnimalType;

    @Builder.Default // 빌더 사용 시 리스트 초기화를 보장
    @ElementCollection
    @CollectionTable(name = "USER_PREFERRED_ANIMALS", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "ANIMAL_TYPE")
    private List<String> preferredAnimalTypes = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "USER_INTERESTS", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "INTEREST")
    private List<String> interests = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "USER_MOVIE_GENRES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "GENRE")
    private List<String> movieGenres = new ArrayList<>();
}