package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.MovieEntities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Genre findGenreByGenre(String genre);


    @Modifying
    @Query(value = "select * from movie_genre where movie_id = ?", nativeQuery = true)
    String findGenreByMovieId(long id);

    @Transactional
    @Modifying
    @Query(value = "delete from movie_genre where movie_id = ? ", nativeQuery = true)
    void removeMovieGenreById(long id);

    @Transactional
    @Modifying
    @Query(value = "insert into movie_genre(movie_id, genre_id) values (?,?)",nativeQuery = true)
    void addMovieGenre(Long id, Long genre_id);

    @Transactional
    @Modifying
    @Query(value = "insert into genre(genre) values (?)",nativeQuery = true)
    void addGenre(String genre);

}
