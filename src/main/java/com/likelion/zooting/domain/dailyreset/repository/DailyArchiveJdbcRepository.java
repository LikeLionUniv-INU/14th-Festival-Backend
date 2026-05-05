package com.likelion.zooting.domain.dailyreset.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class DailyArchiveJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void archiveUsers(LocalDate serviceDate, LocalDateTime archivedAt) {
        jdbcTemplate.update("""
            INSERT INTO user_history (
                service_date,
                archived_at,
                original_user_id,
                created_at,
                gender,
                instagram_id,
                privacy_consent,
                status,
                animal_type_id
            )
            SELECT
                ?,
                ?,
                user_id,
                created_at,
                gender,
                instagram_id,
                privacy_consent,
                status,
                animal_type_id
            FROM `user`
        """, serviceDate, archivedAt);
    }

    public void archiveUserInterests(LocalDate serviceDate, LocalDateTime archivedAt) {
        jdbcTemplate.update("""
            INSERT INTO user_interest_history (
                service_date,
                archived_at,
                original_user_interest_id,
                interest_id,
                original_user_id
            )
            SELECT
                ?,
                ?,
                user_interest_id,
                interest_id,
                user_id
            FROM user_interest
        """, serviceDate, archivedAt);
    }

    public void archiveUserMovieGenres(LocalDate serviceDate, LocalDateTime archivedAt) {
        jdbcTemplate.update("""
            INSERT INTO user_movie_genre_history (
                service_date,
                archived_at,
                original_user_movie_genre,
                movie_genre_id,
                original_user_id
            )
            SELECT
                ?,
                ?,
                user_movie_genre,
                movie_genre_id,
                user_id
            FROM user_movie_genre
        """, serviceDate, archivedAt);
    }

    public void archiveUserAnimalTypes(LocalDate serviceDate, LocalDateTime archivedAt) {
        jdbcTemplate.update("""
            INSERT INTO user_animal_type_history (
                service_date,
                archived_at,
                original_user_animal_type_id,
                animal_type_id,
                original_user_id
            )
            SELECT
                ?,
                ?,
                user_animal_type_id,
                animal_type_id,
                user_id
            FROM user_animal_type
        """, serviceDate, archivedAt);
    }

    public void archiveMatches(LocalDate serviceDate, LocalDateTime archivedAt) {
        jdbcTemplate.update("""
            INSERT INTO match_history (
                service_date,
                archived_at,
                original_match_id,
                animal_num,
                interest_num,
                movie_num,
                score,
                female_user_id,
                male_user_id
            )
            SELECT
                ?,
                ?,
                match_id,
                animal_num,
                interest_num,
                movie_num,
                score,
                female_user_id,
                male_user_id
            FROM matches
        """, serviceDate, archivedAt);
    }
}