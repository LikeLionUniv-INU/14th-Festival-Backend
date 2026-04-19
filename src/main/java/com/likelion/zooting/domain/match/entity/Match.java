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
    @Column(name = "match_id")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "male_user_id")
    private User maleUser;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 동물상을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "female_user_id")
    private User femaleUser;

    @Column(name = "interest_num")
    private Integer interestNum;

    @Column(name = "movie_num")
    private Integer movieNum;

    @Column(name = "score")
    private Integer score;
}


