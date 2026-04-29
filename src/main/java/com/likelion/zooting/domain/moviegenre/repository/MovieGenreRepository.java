package com.likelion.zooting.domain.moviegenre.repository;

import com.likelion.zooting.domain.moviegenre.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre,Long> {
}
