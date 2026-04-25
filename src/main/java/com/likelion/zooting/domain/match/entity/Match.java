package com.likelion.zooting.domain.match.entity;

import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@AllArgsConstructor
@Table(name = "MATCH")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_ID")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "MALE_USER_ID")  // 외래키 설정
    private User maleUser;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "FEMALE_USER_ID")    // 외래키 설정
    private User femaleUser;

    @Column(name = "ANIMAL_NUM")
    private Integer animalNum;  // 동물상 일치 개수

    @Column(name = "INTEREST_NUM")
    private Integer interestNum;    // 관심사 일치 개수

    @Column(name = "movie_num")
    private Integer movieNum;   // 영화 장르 일치 개수

    @Column(name = "score")
    private Integer score;  // 매칭 점수
}


