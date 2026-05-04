package com.likelion.zooting.domain.history.entity;

import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.user.entity.Gender;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "HISTORY")
public class History {
    // Hostory 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private HistoryStatus historyStatus;   // 이력 상태

    @Column(name = "CREATED_AT", updatable = false)
    private java.time.LocalDateTime createdAt;  // 이력 생성 시점

    // Matches 정보
    @Column(name = "ANIMAL_NUM")
    private Integer animalNum;  // 동물상 일치 개수

    @Column(name = "INTEREST_NUM")
    private Integer interestNum;    // 관심사 일치 개수

    @Column(name = "MOVIE_NUM")
    private Integer movieNum;   // 영화 장르 일치 개수

    @Column(name = "SCORE")
    private Integer score;  // 매칭 점수

    // User 정보
    @Column(name = "USER_INSTAGRAM_ID")
    private String userInstagramId;

    @Column(name = "PARTNER_INSTAGRAM_ID")
    private String partnerInstagramId;

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "USER_GENDER", length = 10)
    private Gender userGender;  // 성별

    @Column(name = "USER_ANIMAL_TYPE")
    private String userAnimalType;

    @Column(name = "USER_REGISTER_AT", updatable = false) // 수정 못하게 설정
    private LocalDateTime userRegisterAt;    // 사용자 등록 시간

    @Column(name = "USER_PRIVACY_CONSENT")
    private boolean userPrivacyConsent;

    // 매칭일 경우
    public static History createMatchedUserHistory(User user, User partner, Matches matches, String userAnimalType) {
        History history = new History();
        // 이력 정보 삽입
        history.createdAt = LocalDateTime.now();
        history.historyStatus = HistoryStatus.MATCHED;
        // 매칭 정보 삽입
        history.animalNum = matches.getAnimalNum();
        history.interestNum = matches.getInterestNum();
        history.movieNum = matches.getMovieNum();
        history.score = matches.getScore();
        // 사용자 정보 삽입
        history.userInstagramId = user.getInstagramId();
        history.partnerInstagramId = partner.getInstagramId();
        history.userGender = user.getGender();
        history.userAnimalType = userAnimalType;
        history.userRegisterAt = user.getCreatedAt();
        history.userPrivacyConsent = user.isPrivacyConsent();
        return history;
    }

    public static History createUnmatchedUserHistory(User user, String userAnimalType) {
        History history = new History();
        // 이력 정보 삽입
        history.createdAt = LocalDateTime.now();
        history.historyStatus = HistoryStatus.FAILED;
        // 사용자 정보 삽입
        history.userInstagramId = user.getInstagramId();
        history.userGender = user.getGender();
        history.userAnimalType = userAnimalType;
        history.userRegisterAt = user.getCreatedAt();
        history.userPrivacyConsent = user.isPrivacyConsent();
        return history;
    }

    public static History createErroredUserHistory(User user, String userAnimalType) {
        History history = new History();
        // 이력 정보 삽입
        history.createdAt = LocalDateTime.now();
        history.historyStatus = HistoryStatus.ERROR;
        // 사용자 정보 삽입
        history.userInstagramId = user.getInstagramId();
        history.userGender = user.getGender();
        history.userAnimalType = userAnimalType;
        history.userRegisterAt = user.getCreatedAt();
        history.userPrivacyConsent = user.isPrivacyConsent();
        return history;
    }
}