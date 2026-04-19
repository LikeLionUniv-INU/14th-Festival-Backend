package com.likelion.zooting.domain.match.entity;

import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "MATCH")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY) // DB에서
    @JoinColumn(name = "male_user_id")
    private User maleUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "female_user_id")
    private User femaleUser;

    @Column(name = "interest_num")
    private Integer interestNum;

    @Column(name = "movie_num")
    private Integer movieNum;

    @Column(name = "score")
    private Integer score;
}


