package com.likelion.zooting.domain.usermoviegenre.entity;

import com.likelion.zooting.domain.moviegenre.entity.MovieGenre;
import com.likelion.zooting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@AllArgsConstructor
@Table(name = "USER_MOVIE_GENRE")
public class UserMovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_MOVIE_GENRE")
    private Long userMovieGenreId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 USER를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "user_id")   // 외래키 설정
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)  // 최적화를 위해 MOVIE_GENRE를 DB에서 가져오지 않도록 설정
    @JoinColumn(name = "movie_genre_id")    // 외래키 설정
    private MovieGenre movieGenreId;
}
