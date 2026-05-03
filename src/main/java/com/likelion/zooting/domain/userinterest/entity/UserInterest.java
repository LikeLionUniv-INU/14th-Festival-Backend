package com.likelion.zooting.domain.userinterest.entity;

import com.likelion.zooting.domain.interest.entity.Interest;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "USER_INTEREST")
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_INTEREST_ID")
    private Long userInterestId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER을 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "USER_ID")   // 외래키 설정
    private User user;  // DB로 저장될 땐 PK로 저장되지만, Entity로 가져오므로 user로 지었다.

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 INTEREST를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "INTEREST_ID")   // 외래키 설정
    private Interest interest;  // DB로 저장될 땐 PK로 저장되지만, Entity로 가져오므로 interest로 지었다.

    public UserInterest(User user, Interest interest) {
        this.user = user;
        this.interest = interest;
    }
}
