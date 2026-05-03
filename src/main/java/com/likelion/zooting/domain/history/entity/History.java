package com.likelion.zooting.domain.history.entity;

import com.likelion.zooting.domain.match.entity.Matches;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "USER_ID")  // 외래키 설정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "PARTNER_ID", nullable = true)  // 외래키 설정
    private User partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private HistoryStatus status;   // 이력 상태

    @Column(name = "CREATED_AT", updatable = false)
    private java.time.LocalDateTime createdAt;  // 이력 생성 시점

    @Column(name = "ANIMAL_NUM")
    private Integer animalNum;  // 동물상 일치 개수

    @Column(name = "INTEREST_NUM")
    private Integer interestNum;    // 관심사 일치 개수

    @Column(name = "MOVIE_NUM")
    private Integer movieNum;   // 영화 장르 일치 개수

    @Column(name = "SCORE")
    private Integer score;  // 매칭 점수

    // 매칭일 경우
    public static History createMatchedUserHistory(User user, User partner, Matches matches) {
        History history = new History();
        history.user = user;
        history.partner = partner;
        history.status = HistoryStatus.MATCHED;
        history.createdAt = LocalDateTime.now();
        history.animalNum = matches.getAnimalNum();
        history.interestNum = matches.getInterestNum();
        history.movieNum = matches.getMovieNum();
        history.score = matches.getScore();
        return history;
    }

    public static History createUnmatchedUserHistory(User user) {
        History history = new History();
        history.user = user;
        history.partner = null;
        history.status = HistoryStatus.MATCHED;
        history.createdAt = LocalDateTime.now();
        history.animalNum = null;
        history.interestNum = null;
        history.movieNum = null;
        history.score = null;
        return history;
    }

    public static History createErroredUserHistory(User user) {
        History history = new History();
        history.user = user;
        history.partner = null;
        history.status = HistoryStatus.ERROR;
        history.createdAt = LocalDateTime.now();
        history.animalNum = null;
        history.interestNum = null;
        history.movieNum = null;
        history.score = null;
        return history;
    }
}