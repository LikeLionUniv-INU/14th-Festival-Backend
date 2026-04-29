package com.likelion.zooting.domain.user.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
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

    @Column(name = "USER_PW", length = 4)
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

    protected User(String instagramId, String userPw, boolean privacyConsent) {
        this.instagramId = instagramId;
        this.userPw = userPw;
        this.privacyConsent = privacyConsent;
        this.status = Status.IN_PROGRESS;
    }

    public static User create(String instagramId, String userPw, boolean privacyConsent) {
        return new User(instagramId, userPw, privacyConsent);
    }
}