package com.likelion.zooting.domain.match.entity;

import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "MATCHES")
public class Matches {
    /**
     * {@link Matches} 엔티티 생성을 위한 정적 팩토리 메서드입니다.
     * <p>
     * 외부에서 생성자를 통한 직접적인 객체 생성을 제한(Access Control)하고,
     * 매칭에 필요한 모든 필드를 한 번에 주입받아 객체의 원자성(Atomicity)을 보장합니다.
     * </p>
     *
     * @param maleUser    남성 사용자
     * @param femaleUser  여성 사용자
     * @param animalNum   매칭된 동물상 수
     * @param interestNum 매칭된 관심사 수
     * @param movieNum    매칭된 영화 장르 수
     * @param score       총 매칭 점수
     * @return 매칭 정보가 완성된 Match 인스턴스
     */
    public static Matches create(User maleUser, User femaleUser, Integer animalNum, Integer interestNum, Integer movieNum, Integer score) {
        Matches matches = new Matches();
        matches.maleUser = maleUser;
        matches.femaleUser = femaleUser;
        matches.animalNum = animalNum;
        matches.interestNum = interestNum;
        matches.movieNum = movieNum;
        matches.score = score;
        return matches;
    }

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

    @Column(name = "MOVIE_NUM")
    private Integer movieNum;   // 영화 장르 일치 개수

    @Column(name = "SCORE")
    private Integer score;  // 매칭 점수
}