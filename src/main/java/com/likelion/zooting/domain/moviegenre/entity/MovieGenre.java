package com.likelion.zooting.domain.moviegenre.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@AllArgsConstructor
@Table(name = "MOVIE_GENRE")
public class MovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_GENRE_ID")
    private Long movieGenreId;

    @Column(name = "MOVIE_GENRE_NAME", length = 30)
    private String movieGenreName;  // 영화 장르 이름

}
