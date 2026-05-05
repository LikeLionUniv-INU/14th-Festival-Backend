package com.likelion.zooting.domain.dailyreset.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyResetJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void deleteLiveData() {
        jdbcTemplate.update("DELETE FROM matches");
        jdbcTemplate.update("DELETE FROM user_interest");
        jdbcTemplate.update("DELETE FROM user_movie_genre");
        jdbcTemplate.update("DELETE FROM user_animal_type");
        jdbcTemplate.update("DELETE FROM `user`");
    }
}