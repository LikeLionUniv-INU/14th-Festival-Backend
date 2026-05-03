package com.likelion.zooting.domain.moviegenre.repository;

import com.likelion.zooting.domain.moviegenre.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieGenreRepository extends JpaRepository<MovieGenre,Long> {
  Optional<MovieGenre> findByMovieGenreName(String movieGenreName);
}
