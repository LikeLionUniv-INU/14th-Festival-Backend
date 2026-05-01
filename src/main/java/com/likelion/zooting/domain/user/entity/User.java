package com.likelion.zooting.domain.user.entity;

import com.likelion.zooting.domain.animaltype.entity.AnimalType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 ANIMAL_TYPE을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "ANIMAL_TYPE_ID")    // 외래키 설정
    private AnimalType animalType;  // DB로 저장될 땐 PK로 저장되지만, Entity로 가져오므로 animalType으로 지었다.

    @Column(name = "INSTAGRAM_ID", length = 50)
    private String instagramId; // 인스타 아이디

    @Column(name = "USER_PW", length = 4)
    private String userPw;  // 사용자 PW

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "GENDER", length = 10)
    private Gender gender;  // 성별

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "STATUS", length = 10)
    private Status status;  // 처리 상태

    @Column(name = "CREATED_AT", updatable = false) // 수정 못하게 설정
    private LocalDateTime createdAt;    // 생성 시간

    @Column(name = "PRIVACY_CONSENT")
    private boolean privacyConsent;

    public User(String instagramId, String userPw) {
        this.instagramId = instagramId;
        this.userPw = userPw;
        this.status = Status.IN_PROGRESS;
    }

    public void updatePrivacyConsent(Boolean privacyConsent) {
        this.privacyConsent = privacyConsent;
    }
}
