package com.likelion.zooting.domain.interest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "INTEREST")
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTEREST_ID")
    private Long interestId;

    @Column(name = "INTEREST_NAME", length = 30)
    private String interestName;

    @Column(name = "TAG", length = 30)
    private String tag;
}
